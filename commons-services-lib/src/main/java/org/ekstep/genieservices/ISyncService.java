package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

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

}
