package org.ekstep.genieservices.commons.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;

import java.io.File;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class AndroidDownloadManager implements IDownloadManager {

    private Context mContext;
    private android.app.DownloadManager mDownloadManager;

    public AndroidDownloadManager(Context context) {
        this.mContext = context;
        this.mDownloadManager = (android.app.DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public long enqueue(DownloadRequest downloadRequest) {
        android.app.DownloadManager.Request managerRequest = new android.app.DownloadManager.Request(Uri.parse(downloadRequest.getDownloadUrl()));
        managerRequest.setTitle(downloadRequest.getIdentifier());
        managerRequest.setMimeType(downloadRequest.getMimeType());
        managerRequest.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE);
        managerRequest.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, downloadRequest.getFilename());

        long downloadId = mDownloadManager.enqueue(managerRequest);

        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String localFilePath = file.getAbsolutePath() + "/" + downloadRequest.getFilename();
        downloadRequest.setDownloadedFilePath(localFilePath);

        return downloadId;
    }

    @Override
    public DownloadProgress getProgress(long downloadId) {
        DownloadProgress downloadProgress = new DownloadProgress(downloadId);

        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = mDownloadManager.query(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytesTotal = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
                if (downloadStatus == DownloadManager.STATUS_PENDING) {
                    downloadProgress.setStatus(NOT_STARTED);
                } else if (downloadStatus == DownloadManager.STATUS_FAILED) {
                    downloadProgress.setStatus(FAILED);
                } else if (downloadStatus == DownloadManager.STATUS_RUNNING || downloadStatus == DownloadManager.STATUS_PAUSED) {
                    int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                    --progress; //to ensure that 100% is set only after the download is actually successful
                    downloadProgress.setStatus(STARTED);
                    downloadProgress.setDownloadProgress(progress);
                } else if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                    downloadProgress.setStatus(COMPLETED);
                    downloadProgress.setDownloadProgress(100);
                }
            }
            cursor.close();
        }

        return downloadProgress;
    }

    @Override
    public void cancel(long downloadId) {
        if (downloadId != -1) {
            this.mDownloadManager.remove(downloadId);
        }
    }

}
