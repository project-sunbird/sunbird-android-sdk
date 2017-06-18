package org.ekstep.genieservices.commons.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.utils.FileUtil;

import java.io.FileNotFoundException;

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
    public long enqueue(DownloadRequest request) {
        android.app.DownloadManager.Request managerRequest = new android.app.DownloadManager.Request(Uri.parse(request.getDownloadUrl()));
        managerRequest.setTitle(request.getIdentifier());
        managerRequest.setMimeType(request.getMimeType());
        managerRequest.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE);
        return mDownloadManager.enqueue(managerRequest);
    }

    @Override
    public DownloadProgress getProgress(long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = mDownloadManager.query(query);
        DownloadProgress downloadProgress = new DownloadProgress(downloadId);
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
        return downloadProgress;
    }

    @Override
    public void cancel(long downloadId) {
        this.mDownloadManager.remove(downloadId);
    }

    @Override
    public String getDownloadPath(long downloadId) {
        String downloadPath = null;
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                if (Build.VERSION.SDK_INT >= 24) {
                    ParcelFileDescriptor fileDescriptor = null;
                    try {
                        fileDescriptor = mDownloadManager.openDownloadedFile(downloadId);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    downloadPath = FileUtil.getDownloadedFileLocation(new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor));
                } else {

                    String filepath = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
                    downloadPath = getRealPathFromURI(mContext, Uri.parse(filepath));

                }
            }
        }
        return downloadPath;
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
