package org.ekstep.genieservices.commons.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.download.DownloadQueueManager;
import org.ekstep.genieservices.eventbus.EventPublisher;

import java.io.FileNotFoundException;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadFileReceiver extends BroadcastReceiver {

    private static final String TAG = DownloadFileReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        IDownloadService downloadService = GenieService.getService().getDownloadService();
        String downloadedFilePath = null;
        long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadRequest request = downloadService.getDownloadRequest(downloadId);
        if (request != null) {
            DownloadProgress progress = downloadService.getProgress(request.getIdentifier());
            switch (progress.getStatus()) {
                case IDownloadManager.COMPLETED:
                    String downloadPath = progress.getDownloadPath();
                    //Ideally get the service class from Reflection utils with a service name provided in the downloadRequest
            }
        }
    }

}
