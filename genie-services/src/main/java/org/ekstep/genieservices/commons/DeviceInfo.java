package org.ekstep.genieservices.commons;

import android.content.Context;
import android.provider.Settings;

import org.ekstep.genieservices.commons.utils.CryptoUtil;

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

            return CryptoUtil.checksum(android_id);
        } catch (Exception e) {
            return android_id;
        }
    }

}
