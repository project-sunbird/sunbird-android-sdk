package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;

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
    GenieResponse<SyncStat> sync();

}
