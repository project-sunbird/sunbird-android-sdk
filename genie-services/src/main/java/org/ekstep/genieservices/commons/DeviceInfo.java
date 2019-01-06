package org.ekstep.genieservices.commons;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import org.ekstep.genieservices.commons.bean.telemetry.DeviceSpecification;
import org.ekstep.genieservices.commons.utils.CryptoUtil;
import org.ekstep.genieservices.utils.DeviceSpec;

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

    @Override
    public DeviceSpecification getDeviceDetails() {
        DeviceSpecification deviceSpec = new DeviceSpecification();
        deviceSpec.setOs("Android " + org.ekstep.genieservices.utils.DeviceSpec.getOSVersion());
        deviceSpec.setMake(org.ekstep.genieservices.utils.DeviceSpec.getDeviceName());
        deviceSpec.setId(getDeviceID());

        String internalMemory = DeviceSpec.bytesToHuman(DeviceSpec.getTotalInternalMemorySize());
        if (!TextUtils.isEmpty(internalMemory)) {
            deviceSpec.setIdisk(Double.valueOf(internalMemory));
        }

        String externalMemory = DeviceSpec.bytesToHuman(DeviceSpec.getTotalExternalMemorySize());
        if (!TextUtils.isEmpty(externalMemory)) {
            deviceSpec.setEdisk(Double.valueOf(externalMemory));
        }

        String screenSize = DeviceSpec.getScreenInfoinInch(context);
        if (!TextUtils.isEmpty(screenSize)) {
            deviceSpec.setScrn(Double.valueOf(screenSize));
        }

        String[] cameraInfo = org.ekstep.genieservices.utils.DeviceSpec.getCameraInfo(context);
        String camera = "";
        if (cameraInfo != null) {
            camera = TextUtils.join(",", cameraInfo);
        }
        deviceSpec.setCamera(camera);

        deviceSpec.setCpu(org.ekstep.genieservices.utils.DeviceSpec.getCpuInfo());
        deviceSpec.setSims(-1);
        return deviceSpec;
    }

}
