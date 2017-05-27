package org.ekstep.genieservices.commons;

import android.content.Context;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeviceInfo implements IDeviceInfo {

    private final Context context;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    public String getDeviceID() {
        String android_id = null;
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            return sha1(android_id);
        } catch (Exception e) {
            return android_id;
        }
    }

    private String sha1(String input) {
        MessageDigest mDigest = null;
        StringBuffer sb = new StringBuffer();
        try {
            mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
