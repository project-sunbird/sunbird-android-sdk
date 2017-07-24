package org.ekstep.genieservices.commons.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;

import java.io.File;

/**
 * Created on 13/6/17.
 *
 * @author swayangjit
 */
public class ContentImportService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownloadRequest downloadRequest = (DownloadRequest) intent.getSerializableExtra(ServiceConstants.BundleKey.BUNDLE_KEY_DOWNLOAD_REQUEST);

        File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String filename = downloadRequest.getIdentifier() + "." + ServiceConstants.FileExtension.CONTENT;
        String localFilePath = file.getAbsolutePath() + "/" + filename;

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder();
        ecarImportRequest.fromFilePath(localFilePath)
                .toFolder(downloadRequest.getDestinationFolder());
        if (downloadRequest.isChildContent()) {
            ecarImportRequest.isChildContent();
        }

        IContentService contentService = GenieService.getService().getContentService();
        contentService.importEcar(ecarImportRequest.build());

        // TODO: 7/24/2017
        IDownloadService downloadService = GenieService.getService().getDownloadService();
        downloadService.removeDownloadedFile(downloadRequest.getDownloadId());
        downloadService.resumeDownloads();

        stopSelf();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
