package org.ekstep.genieservices.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created by swayangjit on 1/6/17.
 */

public class ContentUtil {

    public static boolean isAppInstalled(Context context, String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }

        return isInstalled;
    }

    public static void openPlaystore(Context context, String osId) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.android.vending");
        ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
        launchIntent.setComponent(comp);
        launchIntent.setData(Uri.parse("market://details?id=" + osId));
        context.startActivity(launchIntent);
    }
}
