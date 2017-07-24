package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
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

    public TelemetryServiceImpl(AppContext appContext, IUserService userService) {
        super(appContext);
        this.mUserService = userService;
    }

    @Override
    public GenieResponse<Void> saveTelemetry(String eventString) {
        String methodName = "saveTelemetry@TelemetryServiceImpl";
        HashMap params = new HashMap();
        params.put("Event", eventString);
        params.put("logLevel", "2");

        GenieResponse<Void> response;
        try {
            response = saveEvent(eventString);
            return response;
        } catch (InvalidDataException e) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_SAVE_EVENT, TAG, Void.class);
            saveEvent(TelemetryLogger.create(mAppContext, response, TAG, methodName, params, new HashMap()).toString());
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
        HashMap params = new HashMap();
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

        saveEvent(TelemetryLogger.create(mAppContext, genieResponse, TAG, methodName, params, new HashMap()).toString());
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

        EventBus.postEvent(GsonUtil.fromMap(eventModel.getEventMap(), Telemetry.class));
        Logger.i(TAG, "Event saved successfully");
        return GenieResponseBuilder.getSuccessResponse("Event Saved Successfully", Void.class);
    }

    private void decorateEvent(Map<String, Object> event) {
        //Patch the event with proper timestamp
        String version = readVersion(event);
        if (version.equals("1.0")) {
            updateTs(event, DateUtil.getCurrentTimestamp());
        } else if (version.equals("2.0") || version.equals("2.1")) {
            updateEts(event, DateUtil.getEpochTime());
        }

        if (version.equals("2.1")) {
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
            Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
            List<String> tagList = new ArrayList<>();
            tagList.addAll(activeProgramTags);
            etags.put("app", tagList);

            // Patch the partner tags if missing.
            String partnerId = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
            if (!StringUtil.isNullOrEmpty(partnerId)) {
                List<String> partnerTagList;
                if (etags.containsKey("partner")) {
                    partnerTagList = (List<String>) etags.get("partner");
                } else {
                    partnerTagList = new ArrayList<>();
                }

                if (!partnerTagList.contains(partnerId)) {
                    partnerTagList.add(partnerId);
                    event.put("partner", partnerTagList);
                }
            }
        } else {
            //Patch Partner tags
            String partnerId = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
            List<Map<String, Object>> tags = (List<Map<String, Object>>) event.get("tags");
            if (!StringUtil.isNullOrEmpty(partnerId) && !CollectionUtil.containsMap(tags, ServiceConstants.Partner.KEY_PARTNER_ID)) {
                addTagIfMissing(event, ServiceConstants.Partner.KEY_PARTNER_ID, partnerId);
            }

            //Patch Program tags
            Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
            List<String> tagList = new ArrayList<>();
            tagList.addAll(activeProgramTags);
            addTagIfMissing(event, "genie", tagList);
        }

        // Patch the gdata - GameData
        addGameDataIfMissing(event);

        //Patch the event with current Sid and Uid
        if (mUserService != null) {
            UserSession currentUserSession = mUserService.getCurrentUserSession().getResult();
            if (currentUserSession != null && currentUserSession.isValid()) {
                updateSessionDetails(event, currentUserSession.getSid(), currentUserSession.getUid());
            }
        }

        //Patch the event with did
        updateDeviceInfo(event, mAppContext.getDeviceInfo().getDeviceID());
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
        event.put("channel", mAppContext.getParams().getString(ServiceConstants.Params.CHANNEL_ID));
    }

    private void addProducerData(Map<String, Object> event) {
        if (!event.containsKey("pdata")) {
            event.put("pdata", new HashMap<>());
        }

        Map<String, Object> pdata = (Map<String, Object>) event.get("pdata");

        if (!pdata.containsKey("id") ||
                (pdata.containsKey("id") && StringUtil.isNullOrEmpty(String.valueOf(pdata.containsKey("id"))))) {
            pdata.put("id", mAppContext.getParams().getString(ServiceConstants.Params.PRODUCER_ID));
        }

        if (!pdata.containsKey("ver") ||
                (pdata.containsKey("ver") && StringUtil.isNullOrEmpty(String.valueOf(pdata.containsKey("ver"))))) {
            pdata.put("ver", mAppContext.getParams().getString(ServiceConstants.Params.VERSION_NAME));
        }
    }

    private void addGameDataIfMissing(Map<String, Object> event) {
        Map<String, Object> gdata;
        if (event.containsKey("gdata")) {
            gdata = (Map<String, Object>) event.get("gdata");
        } else {
            gdata = new HashMap<>();
            gdata.put("id", mAppContext.getParams().getString(ServiceConstants.Params.APPLICATION_ID));
            gdata.put("ver", mAppContext.getParams().getString(ServiceConstants.Params.VERSION_NAME));

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
