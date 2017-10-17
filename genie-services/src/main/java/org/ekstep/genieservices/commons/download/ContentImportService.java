package org.ekstep.genieservices.commons.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.async.IPerformable;
import org.ekstep.genieservices.async.ThreadPool;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 13/6/17.
 *
 * @author swayangjit
 */
public class ContentImportService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DownloadRequest downloadRequest = (DownloadRequest) intent.getSerializableExtra(ServiceConstants.BundleKey.BUNDLE_KEY_DOWNLOAD_REQUEST);

        final EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder();
        ecarImportRequest.fromFilePath(downloadRequest.getDownloadedFilePath())
                .toFolder(downloadRequest.getDestinationFolder());
        if (downloadRequest.isChildContent()) {
            ecarImportRequest.isChildContent();
        }

        ThreadPool.getInstance().execute(new IPerformable() {
            @Override
            public GenieResponse perform() {
                IContentService contentService = GenieService.getService().getContentService();
                contentService.importEcar(ecarImportRequest.build());

                IDownloadService downloadService = GenieService.getService().getDownloadService();
                downloadService.removeDownloadedFile(downloadRequest.getDownloadId());

                stopSelf();
                return null;
            }
        }, null);


        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
