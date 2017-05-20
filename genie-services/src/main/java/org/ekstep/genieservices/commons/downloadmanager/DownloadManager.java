package org.ekstep.genieservices.commons.downloadmanager;

import android.content.Context;
import android.net.Uri;

import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.Request;

/**
 * Created by swayangjit on 17/5/17.
 */

public class DownloadManager implements IDownloadManager {

    private Context mContext;
    private android.app.DownloadManager mDownloadManager;
    private long mDownloadId;

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
        return mDownloadId;
    }

    @Override
    public void startDownloadProgressTracker() {
        DownloadProgressInfoTask downloadProgressInfoTask = new DownloadProgressInfoTask(mContext,mDownloadId);
        downloadProgressInfoTask.execute();
    }

    @Override
    public void cancel(long downloadId) {
        this.mDownloadManager.remove(downloadId);
    }

}
