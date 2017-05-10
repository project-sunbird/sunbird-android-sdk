package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
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
 * Created on 8/5/17.
 *
 * @author swayangjit
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

        GenieResponse<Map> response = GenieResponseBuilder.getSuccessResponse(getMessage(numberOfSync, numberOfEventsProcessed));

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("fileSize", fileSize);

        response.setResult(objectMap);
        return response;
    }

    /**
     * @return the active sync configuration. It defaults to OVER_WIFI_ONLY when no configuration is active.
     */
    @Override
    public GenieResponse<SyncConfiguration> getConfiguration() {
        String syncConfig = mAppContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.SYNC_CONFIG_SHARED_PREFERENCE_KEY, SyncConfiguration.OVER_WIFI_ONLY.toString());
        SyncConfiguration syncConfiguration = SyncConfiguration.valueOf(syncConfig);
        GenieResponse<SyncConfiguration> genieResponse=GenieResponseBuilder.getSuccessResponse("SyncConfiguraion retrieved successfully");
        genieResponse.setResult(syncConfiguration);
        TelemetryLogger.logSuccess(mAppContext, GenieResponseBuilder.getSuccessResponse("SyncConfiguraion retrieved successfully"), new HashMap(), TAG, "getConfiguration@SyncServiceImpl", new HashMap());
        return genieResponse;
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
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mma", Locale.US);
            syncTime = timeFormat.format(new Date(lastSyncTime));
            genieResponse.setResult(syncTime);
        } else {
            genieResponse.setResult(ServiceConstants.NEVER_SYNCED);
        }

        TelemetryLogger.logSuccess(mAppContext, GenieResponseBuilder.getSuccessResponse("Last sync time fetched successfully"), new HashMap(), TAG, "getLastSyncTime@SyncServiceImpl", new HashMap());
        return genieResponse;
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
    @Override
    public GenieResponse<Boolean> shouldShowSyncPrompt() {
        boolean syncPrompt = isInternetConnected();

        GenieResponse<Boolean> genieResponse = GenieResponseBuilder.getSuccessResponse("Sync prompt event fetched Successfully");
        if (syncPrompt) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put(ServiceConstants.SYNC_PROMPT, syncPrompt);
            genieResponse.setResult(true);
            return genieResponse;
        } else {
            genieResponse.setResult(false);
        }

        TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "shouldShowSyncPrompt@SyncServiceImpl", new HashMap());
        return genieResponse;
    }

    /**
     * @param configuration - this sets the sync configuration to one of the available options.
     *                      the possible options are:
     *                      1. MANUAL
     *                      2. OVER_WIFI_ONLY
     *                      3. OVER_ANY_MODE
     */
    @Override
    public GenieResponse<Void> setConfiguration(SyncConfiguration configuration) {
        HashMap params = new HashMap();
        params.put("configuration", configuration.toString());
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        mAppContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SYNC_CONFIG_SHARED_PREFERENCE_KEY, configuration.toString());
        GenieResponse<Void> genieResponse = GenieResponseBuilder.getSuccessResponse("SyncConfiguration set successfully");
        TelemetryLogger.logSuccess(mAppContext, genieResponse, new HashMap(), TAG, "setConfiguration@SyncServiceImpl", params);
        return genieResponse;
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

    private boolean isInternetConnected() {
        boolean syncPrompt = false;
        IConnectionInfo connectionInfo = mAppContext.getConnectionInfo();
        SyncConfiguration syncConfiguration = getConfiguration().getResult();
        switch (syncConfiguration) {
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
