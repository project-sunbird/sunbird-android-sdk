package org.ekstep.genieservices;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by swayangjit on 7/2/17.
 */

public class EcarCopyUtil {

    public static String createFileFromAsset(Context context, String filePath, String destination) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(filePath);
            Log.e("PATH!!!!", Environment.getExternalStorageDirectory().toString());
            if (!new File(destination).exists()) {
                new File(destination).mkdir();
            }
            File file = new File(destination + File.separator + filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()));
            String tempAssetFilePath = file.getAbsolutePath();
            if (!file.exists()) {
                OutputStream outputStream = new FileOutputStream(tempAssetFilePath);
                byte buffer[] = new byte[1024];
                int length = 0;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            }
            return tempAssetFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}


