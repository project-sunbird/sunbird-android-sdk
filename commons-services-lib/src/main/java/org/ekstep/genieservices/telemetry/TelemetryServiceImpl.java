package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.tag.cache.TelemetryTagCache;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryServiceImpl extends BaseService implements ITelemetryService {

    private static final String TAG = TelemetryServiceImpl.class.getSimpleName();
    private IUserService mUserService=null;

    public TelemetryServiceImpl(AppContext appContext, IUserService userService) {
        super(appContext);
        this.mUserService = userService;
    }

    @Override
    public GenieResponse<Void> saveTelemetry(String eventString) {
        String methodName="saveTelemetry@TelemetryServiceImpl";
        HashMap params = new HashMap();
        params.put("Event", eventString);
        params.put("logLevel", "2");

        try {
            GenieResponse response = saveEvent(eventString);
            saveEvent(TelemetryLogger.create(mAppContext, response, new HashMap(), TAG, methodName, params).toString());
            return response;
        } catch (InvalidDataException e) {
            GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_SAVE_EVENT, TAG, Void.class);
            saveEvent(TelemetryLogger.create(mAppContext, response, new HashMap(), TAG, methodName, params).toString());
            return response;
        }

    }

    @Override
    public GenieResponse<Void> saveTelemetry(Telemetry event) {
        return saveTelemetry(event.toString());
    }

    @Override
    public GenieResponse<TelemetryStat> getTelemetryStat() {
        String methodName="getTelemetryStat@TelemetryServiceImpl";
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
        GenieResponse<TelemetryStat> genieResponse=GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        genieResponse.setResult(new TelemetryStat(unSyncedEventCount,lastSyncTime));

        saveEvent(TelemetryLogger.create(mAppContext, genieResponse, new HashMap(), TAG, methodName, params).toString());

        return genieResponse;
    }


    private GenieResponse saveEvent(String eventString) {
        EventModel event = EventModel.build(mAppContext.getDBSession(), eventString);
        decorateEvent(event);
        event.save();
        EventPublisher.postTelemetryEvent(GsonUtil.fromMap(event.getEventMap(),Telemetry.class));
        Logger.i(TAG, "Event saved successfully");
        return GenieResponseBuilder.getSuccessResponse("Event Saved Successfully", Void.class);
    }

    private void decorateEvent(EventModel event) {

        //Patch the event with proper timestamp
        String version = event.getVersion();
        if (version.equals("1.0")) {
            event.updateTs(DateUtil.getCurrentTimestamp());
        } else if (version.equals("2.0")) {
            event.updateEts(DateUtil.getEpochTime());
        }

        //Patch the event with current Sid and Uid
        if (mUserService != null) {
            UserSession currentUserSession = mUserService.getCurrentUserSession().getResult();
            if (currentUserSession.isValid()) {
                event.updateSessionDetails(currentUserSession.getSid(), currentUserSession.getUid());
            }
        }

        //Patch the event with did
        event.updateDeviceInfo(mAppContext.getDeviceInfo().getDeviceID());


        //Patch Partner tagss
        String values = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.getEventMap().get("tags");
        if (!StringUtil.isNullOrEmpty(values) && !CollectionUtil.containsMap(tags, ServiceConstants.Partner.KEY_PARTNER_ID)) {
            event.addTag(ServiceConstants.Partner.KEY_PARTNER_ID, values);
        }

        //Patch Program tags
        Set<String> activeProgramTags = TelemetryTagCache.activeTags(mAppContext);
        List<String> tagList = new ArrayList<>();
        for (String tag : activeProgramTags) {
            tagList.add(tag);
        }
        event.addTag("genie", tagList);

    }

}
