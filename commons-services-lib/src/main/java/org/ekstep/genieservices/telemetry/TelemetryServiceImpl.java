package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.ArrayUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.tag.cache.TelemetryTagCache;
import org.ekstep.genieservices.eventbus.EventPublisher;
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

    private static final String SERVICE_NAME = TelemetryServiceImpl.class.getSimpleName();
    private IUserService mUserService=null;

    public TelemetryServiceImpl(AppContext appContext, IUserService userService) {
        super(appContext);
        this.mUserService=userService;
    }

    @Override
    public GenieResponse<Void> saveTelemetry(String eventString) {
        String errorMessage = "Not able to save event";
        HashMap params = new HashMap();
        params.put("Event", eventString);
        params.put("logLevel", "3");

        try {
            GenieResponse response = saveEvent(eventString);
            saveEvent(TelemetryLogger.create(mAppContext, response, new HashMap(), SERVICE_NAME, "saveTelemetry@TelemetryServiceImpl", params).toString());
            return response;
        } catch (InvalidDataException e) {
            String logMessage = "Event save failed" + e.toString();
            GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, errorMessage, logMessage, Void.class);
            saveEvent(TelemetryLogger.create(mAppContext, response, new HashMap(), SERVICE_NAME, "saveTelemetry@TelemetryServiceImpl", params).toString());
            return response;
        }

    }

    @Override
    public GenieResponse<Void> saveTelemetry(Telemetry event) {
        return saveTelemetry(event.toString());
    }

    @Override
    public GenieResponse<TelemetryStat> getTelemetryStat() {

        String telemetryEventCountQuery="select count(*) from telemetry";
        String processedTelemetryEventCountQuery="select sum(event_count) from processed_telemetry";
        CustomReaderModel telemetryReaderModel=CustomReaderModel.find(mAppContext.getDBSession(),telemetryEventCountQuery);
        CustomReaderModel processedTelemetryReaderModel=CustomReaderModel.find(mAppContext.getDBSession(),processedTelemetryEventCountQuery);

        int telemetryEventCount=0;
        int processedTelemetryEventCount=0;
        if(telemetryReaderModel!=null){
            telemetryEventCount=Integer.valueOf(telemetryReaderModel.getData());
        }

        if(processedTelemetryReaderModel!=null){
            processedTelemetryEventCount=Integer.valueOf(processedTelemetryReaderModel.getData());
        }

        int unSyncedEventCount=telemetryEventCount+processedTelemetryEventCount;

        IKeyValueStore keyValueStore = mAppContext.getKeyValueStore();
        String syncTime = "";
        if (keyValueStore.contains(ServiceConstants.PreferenceKey.LAST_SYNC_TIME)) {
            Long lastSyncTime = keyValueStore.getLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, 0L);
            if (lastSyncTime == 0) {
                syncTime = ServiceConstants.NEVER_SYNCED;
            }
            else{
                syncTime=DateUtil.format(lastSyncTime,DateUtil.DATE_TIME_AM_PM_FORMAT);
            }
        } else {
            syncTime=ServiceConstants.NEVER_SYNCED;
        }

        GenieResponse<TelemetryStat> genieResponse=GenieResponseBuilder.getSuccessResponse("Telemetry stat retrieved successfully");
        genieResponse.setResult(new TelemetryStat(unSyncedEventCount,syncTime));

        return genieResponse;
    }


    private GenieResponse saveEvent(String eventString) {
        EventModel event = EventModel.build(mAppContext.getDBSession(), eventString);
        decorateEvent(event);
        event.save();
        EventPublisher.postTelemetryEvent(GsonUtil.fromMap(event.getEventMap(),Telemetry.class));
        Logger.i(SERVICE_NAME, "Event saved successfully");
        return GenieResponseBuilder.getSuccessResponse("Event Saved Successfully", Void.class);
    }

    private void decorateEvent(EventModel event){
        //TODO Decorate should only patch fields that are not already present.
        //Patch the event with current Sid and Uid
        if(mUserService!=null){
            UserSession currentUserSession = mUserService.getCurrentUserSession().getResult();
            if (currentUserSession.isValid()) {
                event.updateSessionDetails(currentUserSession.getSid(), currentUserSession.getUid());
            }
        }

        //Patch the event with did
        event.updateDeviceInfo(mAppContext.getDeviceInfo().getDeviceID());

        //Patch the event with proper timestamp
        String version = event.getVersion();
        if (version.equals("1.0")) {
            event.updateTs(DateUtil.getCurrentTimestamp());
        } else if (version.equals("2.0")) {
            event.updateEts(DateUtil.getEpochTime());
        }

        //Patch Partner tagss
        Set<String> values = mAppContext.getKeyValueStore().getStringSet(ServiceConstants.Partner.KEY_PARTNER_ID, null);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.getEventMap().get("tags");
        if (values != null && !values.isEmpty()) {
            if (!ArrayUtil.containsMap(tags, ServiceConstants.Partner.KEY_PARTNER_ID))
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
