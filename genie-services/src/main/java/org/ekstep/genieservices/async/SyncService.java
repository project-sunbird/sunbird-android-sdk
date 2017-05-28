package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;

public class SyncService {
    private ISyncService syncService;
    public SyncService(GenieService genieService) {
        this.syncService = genieService.getSyncService();
    }
    public void sync(IResponseHandler<SyncStat> responseHandler) {
        new AsyncHandler<SyncStat>(responseHandler).execute(new IPerformable<SyncStat>() {
            @Override
            public GenieResponse<SyncStat> perform() {
                return syncService.sync();
            }
        });
    }

}
