package org.ekstep.genieservices.commons.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.Toast;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.R;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.util.DownloadQueueManager;

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
        String downloadedFilePath = null;
        long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        android.app.DownloadManager downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
            Logger.d(TAG, "Download status: " + status);

            switch (status) {
                case android.app.DownloadManager.STATUS_SUCCESSFUL:
                    if (Build.VERSION.SDK_INT >= 24) {
                        ParcelFileDescriptor fileDescriptor = null;
                        try {
                            fileDescriptor = downloadManager.openDownloadedFile(downloadId);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        downloadedFilePath = FileUtil.getDownloadedFileLocation(new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor));
                    } else {

                        String filepath = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
                        downloadedFilePath = getRealPathFromURI(context, Uri.parse(filepath));
                    }
                    break;

                case android.app.DownloadManager.ERROR_INSUFFICIENT_SPACE:
                    Toast.makeText(context, R.string.download_failed_insufficient_space_on_your_device, Toast.LENGTH_LONG).show();
                    break;

                case android.app.DownloadManager.STATUS_FAILED:
                    Toast.makeText(context, R.string.download_failed_insufficient_space_on_your_device, Toast.LENGTH_LONG).show();
                    break;
            }
        }

        DownloadQueueManager downloadQueueManager = new DownloadQueueManager(GenieService.getService().getKeyStore());
        DownloadRequest downloadRequest = downloadQueueManager.getRequest(downloadId);

        String contentIdentifier = null;
        if (downloadRequest != null) {
            contentIdentifier = downloadRequest.getIdentifier();
        }

        EventPublisher.postDownloadResponse(new DownloadResponse(downloadId, contentIdentifier, downloadedFilePath));
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(index);
            }

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
