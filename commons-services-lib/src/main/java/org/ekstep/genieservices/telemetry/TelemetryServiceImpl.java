package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IGroupService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GroupSession;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryImportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.eventbus.EventBus;
import org.ekstep.genieservices.importexport.bean.ExportTelemetryContext;
import org.ekstep.genieservices.importexport.bean.ImportTelemetryContext;
import org.ekstep.genieservices.tag.cache.TelemetryTagCache;
import org.ekstep.genieservices.telemetry.chained.export.AddGeTransferTelemetryExportEvent;
import org.ekstep.genieservices.telemetry.chained.export.CleanCurrentDatabase;
import org.ekstep.genieservices.telemetry.chained.export.CleanupExportedFile;
import org.ekstep.genieservices.telemetry.chained.export.CopyDatabase;
import org.ekstep.genieservices.telemetry.chained.export.CreateMetadata;
import org.ekstep.genieservices.telemetry.chained.imports.AddGeTransferTelemetryImportEvent;
import org.ekstep.genieservices.telemetry.chained.imports.TransportProcessedEventsImportEvent;
import org.ekstep.genieservices.telemetry.chained.imports.UpdateImportedTelemetryMetadata;
import org.ekstep.genieservices.telemetry.chained.imports.ValidateTelemetryMetadata;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class TelemetryServiceImpl extends BaseService implements ITelemetryService {

    private static final String TAG = TelemetryServiceImpl.class.getSimpleName();

    private static final String GENIE_SERVICE_GID = "genieservice.android";

    private IUserService mUserService;
    private IGroupService mGroupService;

    public TelemetryServiceImpl(AppContext appContext, IUserService userService, IGroupService groupService) {
        super(appContext);
        this.mUserService = userService;
        this.mGroupService = groupService;
    }

    @Override
    public GenieResponse<Void> saveTelemetry(String eventString) {
        String methodName = "saveTelemetry@TelemetryServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("Event", eventString);
        params.put("logLevel", "2");

        GenieResponse<Void> response;
        try {
            response = saveEvent(eventString);
            return response;
        } catch (InvalidDataException e) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_SAVE_EVENT, TAG, Void.class);
            saveEvent(TelemetryLogger.create(mAppContext, response, TAG, methodName, params, new HashMap<String, Object>()).toString());
            return response;
        }
    }

    @Override
    public GenieResponse<Void> saveTelemetry(Telemetry event) {
        return saveTelemetry(event.toString());
    }


    @Override
    public GenieResponse<TelemetryStat> getTelemetryStat() {
        String methodName = "getTelemetryStat@TelemetryServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        String telemetryEventCountQuery = "select count(*) from telemetry";
        String processedTelemetryEventCountQuery = "select sum(event_count) from processed_telemetry";
        CustomReaderModel telemetryCountReader = CustomReaderModel.find(mAppContext.getDBSession(), telemetryEventCountQuery);
        CustomReaderModel processedTelemetryCountReader = CustomReaderModel.find(mAppContext.getDBSession(), processedTelemetryEventCountQuery);

        int telemetryEventCount = 0;
        int processedTelemetryEventCount = 0;
        if (telemetryCountReader != null) {
            telemetryEventCount = Integer.valueOf(telemetryCountReader.getData());
        }

        if (processedTelemetryCountReader != null) {
            processedTelemetryEventCount = Integer.valueOf(processedTelemetryCountReader.getData());
        }

        int unSyncedEventCount = telemetryEventCount + processedTelemetryEventCount;

        IKeyValueStore keyValueStore = mAppContext.getKeyValueStore();
        Long lastSyncTime = keyValueStore.getLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, 0L);
        GenieResponse<TelemetryStat> genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        genieResponse.setResult(new TelemetryStat(unSyncedEventCount, lastSyncTime));
        return genieResponse;
    }

    private GenieResponse<Void> saveEvent(String eventString) {
        Map<String, Object> event = GsonUtil.fromJson(eventString, Map.class, ServiceConstants.Event.ERROR_INVALID_EVENT);
        String eventType = (String) event.get("eid");
        if (StringUtil.isNullOrEmpty(eventType)) {
            throw new InvalidDataException(ServiceConstants.Event.ERROR_INVALID_JSON);
        }

        decorateEvent(event);

        EventModel eventModel = EventModel.build(mAppContext.getDBSession(), event, eventType);
        eventModel.save();
        if ("3.0".equals(readVersion(event))) {
            EventBus.postEvent(GsonUtil.fromMap(eventModel.getEventMap(), Telemetry.class));
        }
        Logger.i(TAG, "Event saved successfully");
        return GenieResponseBuilder.getSuccessResponse("Event Saved Successfully", Void.class);
    }

    private void decorateEvent(Map<String, Object> event) {
        //Patch the event with proper timestamp
        String version = readVersion(event);
        if (version.equals("1.0")) {
            updateTs(event, DateUtil.getCurrentTimestamp());
        } else if (version.equals("2.0") || version.equals("2.1") || version.equals("2.2") || version.equals("3.0")) {
            updateEts(event, DateUtil.getEpochTime());
        }

        UserSession currentUserSession = null;
        if (mUserService != null) {
            currentUserSession = mUserService.getCurrentUserSession().getResult();
        }
        if (version.equals("3.0")) {

            //Patch Actor
            if (isSessionValid(currentUserSession)) {
                addActor(event, currentUserSession.getUid());
            } else {
                addActor(event, "");

            }
            // Patch the Context
            patchContext(currentUserSession, event);

            // Patch tags if missing.
            if (!event.containsKey("tags") || CollectionUtil.isNullOrEmpty((List<String>) event.get("tags"))) {
                event.put("tags", new ArrayList<>());
            }

            List<String> tags = (List<String>) event.get("tags");
            // Patch the app tags.
            patchProgramTagsV3(tags);


        } else {
            if (version.equals("2.1") || version.equals("2.2")) {
                // Patch the channel
                addChannel(event);

                // Patch the pdata - ProducerData
                addProducerData(event);

                // Patch etags if missing.
                if (!event.containsKey("etags")) {
                    event.put("etags", new HashMap<>());
                }

                Map<String, Object> etags = (Map<String, Object>) event.get("etags");
                // Patch the app tags.
                patchProgramTags(etags);
                // Patch the partner tags if missing.
                patchPartnerTags(etags);

            } else if (version.equals("1.0")) {
                //Patch Partner tags
                patchProgramTagsV1(event);

                //Patch Program tags
                patchPartnerTagsV1(event);
            }

            // Patch the gdata - GameData
            addGameDataIfMissing(event);

            //Patch the event with current Sid and Uid
            if (currentUserSession != null && currentUserSession.isValid()) {
                updateSessionDetails(event, currentUserSession.getSid(), currentUserSession.getUid());
            }

            //Patch the event with did
            updateDeviceInfo(event, mAppContext.getDeviceInfo().getDeviceID());
        }


    }

    private String readVersion(Map<String, Object> event) {
        return String.valueOf(event.get("ver"));
    }

    private void updateTs(Map<String, Object> event, String timestamp) {
        String ts = (String) event.get("ts");
        if (ts == null || ts.isEmpty()) {
            event.put("ts", timestamp);
        }
    }

    private void updateEts(Map<String, Object> event, long ets) {
        Double _ets;
        try {
            _ets = (Double) event.get("ets");
        } catch (java.lang.ClassCastException e) {
            _ets = null;
        }
        if (_ets == null) {
            event.put("ets", ets);
        } else {
            event.put("ets", Math.round(_ets));
        }
    }

    private void addChannel(Map<String, Object> event) {
        event.put("channel", mAppContext.getParams().getString(IParams.Key.CHANNEL_ID));
    }

    private void addProducerDataV3(Map<String, Object> event) {
        if (!event.containsKey("pdata")) {
            event.put("pdata", new HashMap<>());
        }

        Map<String, Object> pdata = (Map<String, Object>) event.get("pdata");

        if (CollectionUtil.isKeyNotAvailable(pdata, "id")) {
            pdata.put("id", mAppContext.getParams().getString(IParams.Key.PRODUCER_ID));
        }

        String id = (String) pdata.get("pid");
        if (!isValidId(id)) {
            String pid = mAppContext.getParams().getString(IParams.Key.PRODUCER_UNIQUE_ID);
            if (!StringUtil.isNullOrEmpty(pid)) {
                pdata.put("pid", pid);
            } else {
                pdata.put("pid", GENIE_SERVICE_GID);
            }
        }

        if (CollectionUtil.isKeyNotAvailable(pdata, "ver")) {
            pdata.put("ver", mAppContext.getParams().getString(IParams.Key.VERSION_NAME));
        }
    }

    private void addProducerData(Map<String, Object> event) {
        if (!event.containsKey("pdata")) {
            event.put("pdata", new HashMap<>());
        }

        Map<String, Object> pdata = (Map<String, Object>) event.get("pdata");

        if (!pdata.containsKey("id") ||
                (pdata.containsKey("id") && StringUtil.isNullOrEmpty(String.valueOf(pdata.get("id"))))) {
            pdata.put("id", mAppContext.getParams().getString(IParams.Key.PRODUCER_ID));
        }

        if (!pdata.containsKey("ver") ||
                (pdata.containsKey("ver") && StringUtil.isNullOrEmpty(String.valueOf(pdata.get("ver"))))) {
            pdata.put("ver", mAppContext.getParams().getString(IParams.Key.VERSION_NAME));
        }
    }

    private void patchProgramTagsV3(List<String> tags) {
        Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
        List<String> tagList = new ArrayList<>();
        tagList.addAll(activeProgramTags != null ? activeProgramTags : new ArrayList<String>());
        tags.addAll(tagList);
    }

    private void patchProgramTags(Map<String, Object> tags) {
        Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
        List<String> tagList = new ArrayList<>();
        tagList.addAll(activeProgramTags != null ? activeProgramTags : new ArrayList<String>());
        tags.put("app", tagList);
    }

    private void patchProgramTagsV1(Map<String, Object> event) {
        Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
        List<String> tagList = new ArrayList<>();
        tagList.addAll(activeProgramTags != null ? activeProgramTags : new ArrayList<String>());
        addTagIfMissing(event, "genie", tagList);
    }

    private void patchPartnerTags(Map<String, Object> tags) {
        String partnerId = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        if (!StringUtil.isNullOrEmpty(partnerId)) {
            List<String> partnerTagList;
            if (tags.containsKey("partner")) {
                partnerTagList = (List<String>) tags.get("partner");
            } else {
                partnerTagList = new ArrayList<>();
            }

            if (!partnerTagList.contains(partnerId)) {
                partnerTagList.add(partnerId);
                tags.put("partner", partnerTagList);
            }
        }
    }

    private void patchPartnerTagsV1(Map<String, Object> event) {
        String partnerId = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.get("tags");
        if (!StringUtil.isNullOrEmpty(partnerId) && !CollectionUtil.containsMap(tags, ServiceConstants.Partner.KEY_PARTNER_ID)) {
            addTagIfMissing(event, ServiceConstants.Partner.KEY_PARTNER_ID, partnerId);
        }
    }


    private void patchCorrelationDataV3(Map<String, Object> context) {
        if (!context.containsKey("cdata")) {
            context.put("cdata", new ArrayList<Map<String, String>>());
        }
        List<Map<String, String>> cDataList = (List<Map<String, String>>) context.get("cdata");

        //Patch partner cdata
        String partnerId = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        if (!StringUtil.isNullOrEmpty(partnerId)) {
            if (!containsValue(cDataList, partnerId)) {
                Map<String, String> cdata = new HashMap<>();
                cdata.put("type", "partner");
                cdata.put("id", partnerId);
                cDataList.add(cdata);
            }
        }

        //Patch group cdata
        GroupSession groupSession = null;
        if (mGroupService != null) {
            groupSession = mGroupService.getCurrentGroupSession().getResult();
        }

        if (groupSession != null && groupSession.isValid()) {
            if (!containsValue(cDataList, partnerId)) {
                Map<String, String> cdata = new HashMap<>();
                cdata.put("type", "group");
                cdata.put("id", groupSession.getGid());
                cDataList.add(cdata);
            }
        }
    }

    private boolean containsValue(List<Map<String, String>> mapList, String value) {
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, String> map = mapList.get(i);
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    private void patchContext(UserSession session, Map<String, Object> event) {
        if (!event.containsKey("context")) {
            event.put("context", new HashMap<>());
        }
        Map<String, Object> context = (Map<String, Object>) event.get("context");
        context.put("channel", mAppContext.getParams().getString(IParams.Key.CHANNEL_ID));
        addProducerDataV3(context);
        if (!context.containsKey("env") || StringUtil.isNullOrEmpty(context.get("env").toString())) {
            context.put("env", ServiceConstants.Telemetry.DEFAULT_ENVIRONMENT);
        }
        context.put("sid", isSessionValid(session) ? session.getSid() : "");
        context.put("did", mAppContext.getDeviceInfo().getDeviceID());
        patchCorrelationDataV3(context);
    }

    private void addActor(Map<String, Object> event, String uid) {
        if (!event.containsKey("actor")) {
            event.put("actor", new HashMap<>());
        }

        Map<String, String> actorMap = (Map<String, String>) event.get("actor");

        if (CollectionUtil.isKeyNotAvailable(actorMap, "id")) {
            actorMap.put("id", uid);
        }
    }

    private void addGameDataIfMissing(Map<String, Object> event) {
        Map<String, Object> gdata;
        if (event.containsKey("gdata")) {
            gdata = (Map<String, Object>) event.get("gdata");
        } else {
            gdata = new HashMap<>();
            gdata.put("id", mAppContext.getParams().getString(IParams.Key.APPLICATION_ID));
            gdata.put("ver", mAppContext.getParams().getString(IParams.Key.VERSION_NAME));

            event.put("gdata", gdata);
        }

        String id = (String) gdata.get("id");
        if (!isValidId(id)) {
            gdata.put("id", GENIE_SERVICE_GID);
        }
    }

    private boolean isValidId(String gameID) {
        return gameID != null && !gameID.trim().isEmpty();
    }

    private boolean isSessionValid(UserSession userSession) {
        return (userSession != null && userSession.isValid());
    }

    private void updateSessionDetails(Map<String, Object> event, String sid, String uid) {
        event.put("uid", uid);
        event.put("sid", sid);
    }

    private void updateDeviceInfo(Map<String, Object> event, String did) {
        event.put("did", did);
    }

    private void addTagIfMissing(Map<String, Object> event, String key, Object value) {
        Logger.i(TAG, String.format("addTag %s:%s", key, value));
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.get("tags");
        if (tags == null) {
            Logger.i(TAG, "CREATE TAG");
            tags = new ArrayList<>();
            tags.add(map);

            event.put("tags", tags);
        } else {
            Logger.i(TAG, "EDIT TAG");
            tags.add(map);
        }
    }

    @Override
    public GenieResponse<Void> importTelemetry(TelemetryImportRequest telemetryImportRequest) {
        GenieResponse<Void> response;
        if (!FileUtil.doesFileExists(telemetryImportRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Telemetry event import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(telemetryImportRequest.getSourceFilePath());
        if (ServiceConstants.FileExtension.TELEMETRY.equals(ext)) {
            ImportTelemetryContext importTelemetryContext = new ImportTelemetryContext(telemetryImportRequest.getSourceFilePath());

            ValidateTelemetryMetadata validateTelemetryMetadata = new ValidateTelemetryMetadata();
            validateTelemetryMetadata.then(new TransportProcessedEventsImportEvent())
                    .then(new UpdateImportedTelemetryMetadata())
                    .then(new AddGeTransferTelemetryImportEvent());

            return validateTelemetryMetadata.execute(mAppContext, importTelemetryContext);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Telemetry event import failed, unsupported file extension", TAG);
            return response;
        }
    }

    @Override
    public GenieResponse<TelemetryExportResponse> exportTelemetry(TelemetryExportRequest telemetryExportRequest) {
        File destinationFolder = new File(telemetryExportRequest.getDestinationFolder());
        String destinationDBFilePath = FileUtil.getExportTelemetryFilePath(destinationFolder);

        if (FileUtil.doesFileExists(destinationDBFilePath)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "File already exists.", TAG);
        }

        // Process the event before exporting.
        EventProcessorFactory.processEvents(mAppContext);

        ExportTelemetryContext exportTelemetryContext = new ExportTelemetryContext(telemetryExportRequest.getDestinationFolder(), destinationDBFilePath);
        CopyDatabase copyDatabase = new CopyDatabase();
        copyDatabase.then(new CreateMetadata())
                .then(new CleanupExportedFile())
                .then(new CleanCurrentDatabase())
                .then(new AddGeTransferTelemetryExportEvent());

        // TODO: 6/12/2017 - if export failed.
//                .then(new RemoveExportFile(destinationDBFilePath));

        return copyDatabase.execute(mAppContext, exportTelemetryContext);
    }

}
