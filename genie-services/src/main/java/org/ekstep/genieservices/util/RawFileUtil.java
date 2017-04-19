package org.ekstep.genieservices.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by swayangjit on 18/4/17.
 */

public class RawFileUtil {

    private static final String TAG = RawFileUtil.class.getSimpleName();

    public static String readRawResource(Context context, int rawId) {
        InputStream inputStream = context.getResources().openRawResource(rawId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;

        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toString();
    }
}
