package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.SyncConfiguration;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;
import org.ekstep.genieservices.telemetry.network.TelemetrySyncAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 8/5/17.
 */

public class SyncService extends BaseService {

    private static final String TAG = TelemetryService.class.getSimpleName();

    public SyncService(AppContext appContext) {
        super(appContext);
    }


    public void sync(IResponseHandler responseHandler) {
        HashMap params = new HashMap();
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));

//        if(getConfiguration().canSync(mAppContext.getConnectionInfo()))
        new ExportAdapter(mAppContext).export(null);
        int numberOfSync = 0;
        int numberOfEventsProcessed = 0;

        ProcessedEventModel processedEvent = ProcessedEventModel.build(mAppContext.getDBSession());

        long totalByteSize = 0;

        while (!processedEvent.isEmpty()) {
            totalByteSize = totalByteSize + processedEvent.getData().length;
            GenieResponse response = new TelemetrySyncAPI(mAppContext, processedEvent.getData()).post();

            if (!response.getStatus()) {
                String error = response.getError();
                List<String> errorMessages = response.getErrorMessages();
                String message = getMessage(numberOfSync, numberOfEventsProcessed);
                response.setMessage(message);
            }

            numberOfSync++;
            numberOfEventsProcessed += processedEvent.getNumberOfEvents();

            processedEvent.clear();
            processedEvent = ProcessedEventModel.build(mAppContext.getDBSession());
        }

        String fileSize = calculateByteCountInKB(totalByteSize);
        mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SYNC_FILE_SIZE, fileSize);
        mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, new Date().getTime());

        GenieResponse response = GenieResponse.getSuccessResponse(getMessage(numberOfSync, numberOfEventsProcessed));

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("fileSize", fileSize);

        response.setResult(objectMap);
    }

    private String calculateByteCountInKB(long bytes) {
        try {
            int unit = 1024;
            return String.format("%.2f", bytes / Math.pow(unit, 1));
        } catch (Exception e) {
            return "0.00";
        }
    }

    private String getMessage(int numberOfSync, int numberOfEventsProcessed) {
        return String.format(Locale.US, "%d events synced in %d sync", numberOfEventsProcessed, numberOfSync);
    }

    private GenieResponse getErrorResponse(String error, String errorMessages, String message) {
        return GenieResponse.getErrorResponse("failed", error, errorMessages, String.class);
    }

    /**
     * @return the active sync configuration. It defaults to OVER_WIFI_ONLY when no configuration is active.
     */
    public SyncConfiguration getConfiguration() {
        String syncConfig = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.SYNC_CONFIG_SHARED_PREFERENCE_KEY, SyncConfiguration.OVER_WIFI_ONLY.toString());
        SyncConfiguration syncConfiguration = SyncConfiguration.valueOf(syncConfig);
        TelemetryLogger.logSuccess(mAppContext, GenieResponse.getSuccessResponse("SyncConfiguraion retrieved successfully"), new HashMap(), TAG, "getConfiguration@SyncService", new HashMap());
        return syncConfiguration;
    }

    /**
     * @param configuration - this sets the sync configuration to one of the available options.
     *                      the possible options are:
     *                      1. MANUAL
     *                      2. OVER_WIFI_ONLY
     *                      3. OVER_ANY_MODE
     */
    public void setConfiguration(SyncConfiguration configuration) {
        HashMap params = new HashMap();
        params.put("configuration", configuration.toString());
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SYNC_CONFIG_SHARED_PREFERENCE_KEY, configuration.toString());

        TelemetryLogger.logSuccess(mAppContext, GenieResponse.getSuccessResponse("SyncConfiguraion set successfully"), new HashMap(), TAG, "setConfiguration@SyncService", params);

    }

    /**
     * @return the formatted time of sync. Sample format : "24 May 2016, 3:32pm"
     */
    public String getLastSyncTime() {
        IKeyValueStore keyValueStore = mAppContext.getKeyValueStore();
        if (keyValueStore.contains(ServiceConstants.PreferenceKey.LAST_SYNC_TIME)) {
            Long lastSyncTime = keyValueStore.getLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, 0L);
            if (lastSyncTime == 0)
                return ServiceConstants.NEVER_SYNCED;
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mma");
            return timeFormat.format(new Date(lastSyncTime));
        }
        TelemetryLogger.logSuccess(mAppContext, GenieResponse.getSuccessResponse("Last sync time fetched successfully"), new HashMap(), TAG, "getLastSyncTime@SyncService", new HashMap());
        return ServiceConstants.NEVER_SYNCED;
    }

    /**
     * show data sync prompt if the user is connected to the internet and enabled the sync mode is "Manual"
     * or
     * The sync mode is set to “Automatic over Wifi” and the user is connected to 2G/3G/4G.
     *
     * @return True - 1. If the sync setting is set to “Manual” and the user is connected to internet Or
     * 2. If the sync setting is set to “Automatic over Wifi” and the user is connected to 2G/3G/4G.
     * False - if the user is not connected to the internet or  connected to other mode.
     */
    public void shouldShowSyncPrompt(IResponseHandler responseHandler) {
        boolean syncPrompt = isInternetConnected();
        if (syncPrompt) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ServiceConstants.SYNC_PROMPT, syncPrompt);
            GenieResponse genieResponse = GenieResponse.getSuccessResponse("Sync prompt event fetched Successfully");
            genieResponse.setResult(map);
            responseHandler.onSuccess(genieResponse);
            TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "shouldShowSyncPrompt@SyncService", new HashMap());
        }
    }


    /**
     * @return TRUE - 1. If the sync setting is set to “Manual” and the user is connected to internet Or
     * 2. If the sync setting is set to “Automatic over Wifi” and the user is connected to 2G/3G/4G.
     * False - if the user is not connected to the internet or  connected to other mode.
     */
    private boolean isInternetConnected() {
        boolean syncPrompt = false;
        IConnectionInfo connectionInfo = mAppContext.getConnectionInfo();
        switch (getConfiguration()) {
            case MANUAL:
                if (connectionInfo.isConnected()) {
                    syncPrompt = true;
                }
                break;
            case OVER_WIFI_ONLY:
                if (connectionInfo.isConnectedOverWifi()) {
                    syncPrompt = false;
                } else if (connectionInfo.isConnected()) {
                    syncPrompt = true;
                }
                break;
            case OVER_ANY_MODE:
                syncPrompt = false;
                break;
            default:
                break;
        }
        return syncPrompt;
    }

}
