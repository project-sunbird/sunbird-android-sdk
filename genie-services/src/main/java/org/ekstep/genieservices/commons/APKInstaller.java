package org.ekstep.genieservices.commons;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;

import java.io.File;

/**
 * Created on 6/23/2017.
 *
 * @author anil
 */
public class APKInstaller implements IAPKInstaller {

    private static final String TAG = APKInstaller.class.getSimpleName();

    private AppContext<Context> appContext;
    private PackageManager packageManager;

    public APKInstaller(AppContext<Context> appContext) {
        this.appContext = appContext;
        this.packageManager = appContext.getContext().getPackageManager();
    }

    @Override
    public void installApk(String apkFilePath) {

        File apkFile = null;
        if (!StringUtil.isNullOrEmpty(apkFilePath)) {
            apkFile = new File(apkFilePath);
        }

        if (apkFile != null && apkFile.exists()) {
            String apkPath = apkFile.getAbsolutePath();
            PackageInfo info = packageManager.getPackageArchiveInfo(apkPath, 0);
            String appId = info.packageName;
            if (!isAppInstalled(appId)) {
                showInstallAPKPrompt(apkFile.getAbsolutePath());
            }
        }
    }

    /**
     * Install a apk from local files.
     *
     * @param apkFilePath - Local apk file path
     */
    private void showInstallAPKPrompt(String apkFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), ContentConstants.MimeType.APK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.getContext().startActivity(intent);
    }

    /**
     * To check whether the app is installed or not.
     *
     * @param packageName - Application package name.
     * @return true in installed else false.
     */
    private boolean isAppInstalled(String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            return false;
        }
        boolean installed;
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            Logger.e(TAG, "isAppInstalled", e);
        }

        return installed;
    }
}
