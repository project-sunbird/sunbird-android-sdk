package org.ekstep.genieservices.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 1/6/17.
 *
 * @author swayangjit
 */
public class ContentUtil {

    private static final String TAG = ContentUtil.class.getSimpleName();

    public static boolean isAppInstalled(Context context, String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            return false;
        }

        boolean isInstalled;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Logger.e(TAG, e.getMessage(), e);
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
