package org.ekstep.genieservices.commons.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;

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
        long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadRequest request = downloadService.getDownloadRequest(downloadId);
        if (request != null) {
            DownloadProgress progress = downloadService.getProgress(request.getIdentifier());
            String localFilePath = progress.getDownloadPath();
            switch (progress.getStatus()) {
                case IDownloadManager.COMPLETED:
                    downloadService.onDownloadComplete(request.getIdentifier());
                    //Ideally get the service class from Reflection utils with a service name provided in the downloadRequest
                    Class _class = ReflectionUtil.getClass(request.getProcessorClass());
                    Intent serviceIntent = new Intent(context, _class);
                    serviceIntent.putExtra(ServiceConstants.BUNDLE_KEY_IS_CHILD, request.isChildContent());
                    serviceIntent.putExtra(ServiceConstants.BUNDLE_KEY_LOCAL_FILE_PATH, localFilePath);
                    context.startService(serviceIntent);
                    break;
                case IDownloadManager.FAILED:
                    downloadService.onDownloadFailed(request.getIdentifier());
                    break;

            }
        }
    }

}
