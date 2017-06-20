package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;


/**
 * This class provides APIs for performing Sync related operations on a separate thread
 *
 */
public class SyncService {
    private ISyncService syncService;
    public SyncService(GenieService genieService) {
        this.syncService = genieService.getSyncService();
    }

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
     * @param responseHandler -{@link IResponseHandler<SyncStat>}
     */
    public void sync(IResponseHandler<SyncStat> responseHandler) {
        new AsyncHandler<SyncStat>(responseHandler).execute(new IPerformable<SyncStat>() {
            @Override
            public GenieResponse<SyncStat> perform() {
                return syncService.sync();
            }
        });
    }

}
