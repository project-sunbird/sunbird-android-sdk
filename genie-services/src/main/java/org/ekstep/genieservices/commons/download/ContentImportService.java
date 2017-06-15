package org.ekstep.genieservices.commons.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created by swayangjit on 13/6/17.
 */

public class ContentImportService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IDownloadService downloadService = GenieService.getService().getDownloadService();

        boolean isChild = intent.getBooleanExtra(ServiceConstants.BundleKey.BUNDLE_KEY_IS_CHILD, false);
        String localFilePath = intent.getStringExtra(ServiceConstants.BundleKey.BUNDLE_KEY_LOCAL_FILE_PATH);
        String destination = intent.getStringExtra(ServiceConstants.BundleKey.BUNDLE_KEY_DESTINATION_FILE_PATH);
        IContentService contentService = GenieService.getService().getContentService();
        EcarImportRequest.Builder builder = new EcarImportRequest.Builder();
        if (isChild) {
            builder.isChildContent();
        }
        builder.toFolder(destination);
        builder.fromFilePath(localFilePath);
        EcarImportRequest ecarImportRequest = builder.build();

        GenieResponse<Void> genieResponse = contentService.importEcar(ecarImportRequest);
        if (genieResponse.getStatus()) {
            downloadService.resumeDownloads();
        }
        stopSelf();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
