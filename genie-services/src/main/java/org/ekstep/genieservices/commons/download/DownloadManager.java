package org.ekstep.genieservices.commons.download;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.Request;
import org.ekstep.genieservices.content.download.DownloadQueueManager;
import org.ekstep.genieservices.eventbus.EventPublisher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadManager implements IDownloadManager {

    private Context mContext;
    private android.app.DownloadManager mDownloadManager;
    private long mDownloadId;
    private ScheduledExecutorService mExecutor;

    public DownloadManager(Context context) {
        this.mContext = context;
        this.mDownloadManager = (android.app.DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public long enqueue(Request request) {
        android.app.DownloadManager.Request managerRequest = new android.app.DownloadManager.Request(Uri.parse(request.getUrl()));
        managerRequest.setTitle(request.getTitle());
        managerRequest.setMimeType(request.getMimeType());
        managerRequest.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        mDownloadId = mDownloadManager.enqueue(managerRequest);
        startTrackingProgress(mDownloadId);
        return mDownloadId;
    }

    @Override
    public DownloadProgress query(long downloadId) {

        return getProgress(downloadId);
    }

    @Override
    public void cancel(long downloadId) {
        this.mDownloadManager.remove(downloadId);
    }

    private DownloadProgress getProgress(long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = mDownloadManager.query(query);
        cursor.moveToFirst();
        DownloadProgress downloadProgress = null;
        try {
            DownloadQueueManager queueManager = new DownloadQueueManager(GenieService.getService().getKeyStore());
            DownloadRequest downloadRequest = queueManager.getRequest(downloadId);

            int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytesTotal = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
            if (downloadStatus == android.app.DownloadManager.STATUS_RUNNING) {
                int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                if (downloadRequest != null) {
                    downloadProgress = new DownloadProgress(downloadId, downloadRequest.getIdentifier(), progress);
                }
            } else if (downloadStatus == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                downloadProgress = new DownloadProgress(downloadId, null, 100);
            }
            cursor.close();
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return downloadProgress;
    }


    private void startTrackingProgress(final long downloadId) {
        mExecutor = Executors.newScheduledThreadPool(1);
        mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DownloadProgress downloadProgress = getProgress(downloadId);
                if (downloadProgress.getDownloadProgress() != 100) {
                    EventPublisher.postDownloadProgress(downloadProgress);
                } else {
                    mExecutor.shutdown();
                }

            }
        }, 2, 10, TimeUnit.SECONDS);
    }

}
