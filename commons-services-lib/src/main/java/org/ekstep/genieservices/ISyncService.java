package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.telemetry.SyncConfiguration;

import java.util.Map;

/**
 * This is the interface with all the required APIs for performing Sync related
 * operations.
 */
public interface ISyncService {

    /**
     * This api syncs all the saved telemetry data to the server on the cloud.
     * <p>
     * <p>
     * On successful syncing the data, the response will return status as TRUE and with the volume of the file size that is synced, set in the result.
     * <p>
     * <p>
     * On failing to sync the data, the response will return status as FALSE and the response will have status as FALSE with the following errors:
     * <p> NETWORK_ERROR
     * <p> AUTHENTICATION_ERROR
     * <p> VALIDATION_ERROR
     *
     * @return {@link GenieResponse<Map>}
     */
    GenieResponse<Map> sync();

    /**
     * This api gets the sync configurations and it defaults to OVER_WIFI_ONLY when no configuration is active.
     * <p>
     * <p>On successful fetching the configurations, the response will return status as TRUE and with the message "SyncConfiguration retrieved successfully".
     * <p>
     * <p>Their is no fail case with this api, as it would by default fetch the OVER_WIFI_ONLY configuration
     *
     * @return {@link GenieResponse<SyncConfiguration>}
     */
    GenieResponse<SyncConfiguration> getConfiguration();

    /**
     * This api sets the configuration required to sync telemetry data.
     * <p>
     * <p>On successful setting the configurations, the response will return status as TRUE and with the message "SyncConfiguration set successfully".
     *
     * @param configuration
     * @return
     */
    GenieResponse<Void> setConfiguration(SyncConfiguration configuration);

    /**
     * This api gets the last sync time, if never done, then it will be "NEVER".
     * <p>
     * <p>On successful fetching the last sync time, the response will return status as TRUE and with the message "Last sync time fetched successfully", and sync time set in the result, in "dd/MM/yyyy, hh:mma" format.
     * <p>
     * <p>
     * <p> Their is no fail case with this api, as the last sync time will be "NEVER", if never synced, by default in the response
     *
     * @return {@link GenieResponse<String>}
     */
    GenieResponse<String> getLastSyncTime();

    /**
     * This api will help showing the sync prompt, based on the internet connectivity.
     * <p>
     * <p>
     * <p>On successful fetching to check if the sync prompt has to be showed or not, the response will return the status as TRUE, with following results set based on the internet connectivity
     * <p> Result set to TRUE,  if the sync setting is set to “Manual” and the user is connected to internet Or if the sync setting is set to “Automatic over Wifi” and the user is connected to 2G/3G/4G.
     * <p> Result set to FALSE, if the user is not connected to the internet or connected to other mode.
     * <p>
     * <p>
     * <p> Their is no fail case with this api.
     *
     * @return {@link GenieResponse<Boolean>}
     */
    GenieResponse<Boolean> shouldShowSyncPrompt();
}
