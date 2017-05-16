package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.GEInteract;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;
import org.ekstep.genieservices.telemetry.network.TelemetrySyncAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the implementation of the interface {@link ISyncService}
 */
public class SyncServiceImpl extends BaseService implements ISyncService {

    private static final String TAG = TelemetryServiceImpl.class.getSimpleName();

    public SyncServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Map> sync() {
        HashMap params = new HashMap();
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));

        new ExportAdapter(mAppContext).export(null);
        int numberOfSync = 0;
        int numberOfEventsProcessed = 0;

        ProcessedEventModel processedEvent = ProcessedEventModel.build(mAppContext.getDBSession());

        long totalByteSize = 0;

        while (!processedEvent.isEmpty()) {
            totalByteSize = totalByteSize + processedEvent.getData().length;
            GenieResponse response = new TelemetrySyncAPI(mAppContext, processedEvent.getData()).post();

            if (!response.getStatus()) {
                String message = getMessage(numberOfSync, numberOfEventsProcessed);
                response.setMessage(message);
                return response;
            }

            numberOfSync++;
            numberOfEventsProcessed += processedEvent.getNumberOfEvents();

            processedEvent.clear();
            processedEvent = ProcessedEventModel.build(mAppContext.getDBSession());
        }

        String fileSize = calculateByteCountInKB(totalByteSize);
        mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SYNC_FILE_SIZE, fileSize);
        mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, DateUtil.getEpochTime());

        GenieResponse<Map> response = GenieResponseBuilder.getSuccessResponse(getMessage(numberOfSync, numberOfEventsProcessed));

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("fileSize", fileSize);

        response.setResult(objectMap);
        return response;
    }


    @Override
    public GenieResponse<String> getLastSyncTime() {
        IKeyValueStore keyValueStore = mAppContext.getKeyValueStore();
        String syncTime = "";
        GenieResponse<String> genieResponse = GenieResponseBuilder.getSuccessResponse("Last sync time fetched successfully");
        if (keyValueStore.contains(ServiceConstants.PreferenceKey.LAST_SYNC_TIME)) {
            Long lastSyncTime = keyValueStore.getLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, 0L);
            if (lastSyncTime == 0) {
                syncTime = ServiceConstants.NEVER_SYNCED;
            }
            genieResponse.setResult(DateUtil.format(lastSyncTime,DateUtil.DATE_TIME_AM_PM_FORMAT));
        } else {
            genieResponse.setResult(ServiceConstants.NEVER_SYNCED);
        }

        TelemetryLogger.logSuccess(mAppContext, GenieResponseBuilder.getSuccessResponse("Last sync time fetched successfully"), new HashMap(), TAG, "getLastSyncTime@SyncServiceImpl", new HashMap());
        return genieResponse;
    }

    private void logGEInteractInitiate(String stageId, String subType, InteractionType touchType) {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getGDataId(), mAppContext.getGDataVersionName())).
                stageId(stageId).
                subType(subType).
                interActionType(touchType).build();
        TelemetryLogger.log(geInteract);
    }

    private void logGEInteractSuccess(String stageId, String subType, InteractionType touchType, Map<String, Object> eks) {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getGDataId(), mAppContext.getGDataVersionName())).
                stageId(stageId).
                subType(subType).
                interActionType(touchType).values(eks).build();
        TelemetryLogger.log(geInteract);
    }

    private String calculateByteCountInKB(long bytes) {
        try {
            int unit = 1024;
            return String.format(Locale.US, "%.2f", bytes / Math.pow(unit, 1));
        } catch (Exception e) {
            return "0.00";
        }
    }

    private String getMessage(int numberOfSync, int numberOfEventsProcessed) {
        return String.format(Locale.US, "%d events synced in %d sync", numberOfEventsProcessed, numberOfSync);
    }

    private GenieResponse getErrorResponse(String error, String errorMessages, String message) {
        return GenieResponseBuilder.getErrorResponse("failed", error, errorMessages, String.class);
    }

}
