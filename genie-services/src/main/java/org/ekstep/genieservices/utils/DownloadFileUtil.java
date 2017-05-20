package org.ekstep.genieservices.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by swayangjit on 20/5/17.
 */

public class DownloadFileUtil {

    //TODO will have to decide where to put all these
    private static String getRealPathFromURI(Context context, Uri contentUri) {
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


    public static String getDownloadedFilePath(Context context, Intent intent) {
        String filePath = null;
        android.app.DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Bundle extras = intent.getExtras();
        long downloadId = extras.getLong(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        if (Build.VERSION.SDK_INT >= 24) {
            ParcelFileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = downloadManager.openDownloadedFile(downloadId);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            filePath = FileUtil.getDownloadedFileLocation(new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor));
        } else {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                String filepath = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
                filePath = getRealPathFromURI(context, Uri.parse(filepath));
            }

        }
        return filePath;
    }
}
