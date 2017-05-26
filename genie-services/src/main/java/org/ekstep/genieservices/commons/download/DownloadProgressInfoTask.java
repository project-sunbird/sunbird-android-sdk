package org.ekstep.genieservices.commons.download;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.content.download.DownloadQueueManager;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadProgressInfoTask extends AsyncTask<Void, Integer, Void> {

    private DownloadRequest mDownloadRequest;
    private android.app.DownloadManager downloadManager;

    public DownloadProgressInfoTask(Context context, long downloadId) {
        this.mDownloadRequest = new DownloadQueueManager(GenieService.getService().getKeyStore()).getRequest(downloadId);
        downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        boolean downloading;

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
        EventPublisher.postDownloadProgress(new DownloadProgress(mDownloadRequest.getDownloadId(),mDownloadRequest.getIdentifier(), values[0]));
    }

}
