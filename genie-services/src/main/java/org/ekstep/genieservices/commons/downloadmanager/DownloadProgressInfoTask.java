package org.ekstep.genieservices.commons.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.util.DownloadQueueManager;

/**
 * Created by swayangjit on 20/5/17.
 */

public class DownloadProgressInfoTask extends AsyncTask<Void, Integer, Void> {

    private Context mContext = null;
    private DownloadRequest mDownloadRequest;

    public DownloadProgressInfoTask(Context context, long id) {
        this.mContext = context;
        this.mDownloadRequest = new DownloadQueueManager(GenieService.getService().getKeyStore()).getRequest(id);
    }

    @Override
    protected Void doInBackground(Void... params) {
        boolean downloading;
        final android.app.DownloadManager downloadManager = getDownloadManager();
        do {
            android.app.DownloadManager.Query q = new android.app.DownloadManager.Query();
            q.setFilterById(mDownloadRequest.getDownloadId());

            downloading = true;

            Cursor cursor = downloadManager.query(q);
            cursor.moveToFirst();

            try {
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytesTotal = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS)) == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }

                if (bytesTotal == 0) {
                    downloading = false;
                    publishProgress(-1);
                } else {
                    int downloadProgress = (int) ((bytesDownloaded * 100L) / bytesTotal);

                    publishProgress(downloadProgress);
                }

                cursor.close();
            } catch (CursorIndexOutOfBoundsException e) {
                downloading = false;
                publishProgress(-1);
            }

        } while (downloading && !isCancelled());

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        EventPublisher.postDownloadProgress(new DownloadProgress(mDownloadRequest.getIdentifier(), mDownloadRequest.getDownloadId(), values[0]));
    }


    private android.app.DownloadManager getDownloadManager() {
        return (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

}
