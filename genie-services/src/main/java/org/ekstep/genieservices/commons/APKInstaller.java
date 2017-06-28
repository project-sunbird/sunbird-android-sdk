package org.ekstep.genieservices.commons;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by anil on 6/23/2017.
 */

public class APKInstaller implements IAPKInstaller {

    private static final String TAG = APKInstaller.class.getSimpleName();

    private AppContext<Context> appContext;

    public APKInstaller(AppContext<Context> appContext) {
        this.appContext = appContext;
    }

    /**
     * show install apk prompt, if the apk is not installed...
     *
     * @param destPath      - Content path
     * @param downloadUrl   - Content download url
     * @param preRequisites - Content preRequisites items..
     */
    @Override
    public void showInstallAPKPrompt(String destPath, String downloadUrl, List<Map<String, Object>> preRequisites) {
        try {
            File payloadDestination = getFilePath(destPath, downloadUrl);
            isAllApkInstalled(payloadDestination, preRequisites);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFilePath(String path, String downloadUrl) {
        return new File(path, downloadUrl);
    }

    private boolean isAllApkInstalled(File payloadDestination, List<Map<String, Object>> preRequisitesList) {
        PackageManager manager = appContext.getContext().getPackageManager();

        boolean isReturn = checkApkInstallation(payloadDestination, manager);

        if (preRequisitesList != null && preRequisitesList.size() > 0) {
            for (int i = 0; i < preRequisitesList.size(); i++) {
                Map<String, Object> prerequisites = preRequisitesList.get(i);
                if (prerequisites != null) {
                    ContentModel contentModel = ContentModel.find(appContext.getDBSession(), prerequisites.get("identifier"));
                    if (contentModel != null) {
                        String path = contentModel.getPath();
                        if (!StringUtil.isNullOrEmpty(path)) {
                            String downloadUrl = ContentHandler.getDownloadUrl(GsonUtil.fromJson(contentModel.getLocalData(), Map.class));
                            File filePath = getFilePath(path, downloadUrl);
                            boolean isInstalled = checkApkInstallation(filePath, manager);
                            if (isReturn) {
                                isReturn = isInstalled;
                            }
                        }
                    }
                }
            }
        }

        return isReturn;
    }

    private boolean checkApkInstallation(File payloadDestination, PackageManager manager) {
        boolean isReturn = true;

        if (payloadDestination != null && payloadDestination.exists()) {
            String ext = FileUtil.getFileExtension(payloadDestination.getPath());
            if (ext.contains(ServiceConstants.FileExtension.APK)) {
                String apkPath = payloadDestination.getAbsolutePath();
                PackageInfo info = manager.getPackageArchiveInfo(apkPath, 0);
                String appId = info.packageName;
                if (!isAppInstalled(appId)) {
                    installApk(payloadDestination.getAbsolutePath());
                    isReturn = false;
                }
            }
        } else {
            isReturn = false;
        }
        return isReturn;
    }

    /**
     * Install a apk from local files.
     *
     * @param apkFilePath - Local apk file path
     */
    private void installApk(String apkFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), ContentConstants.MimeType.APPLICATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.getContext().startActivity(intent);
    }

    /**
     * To check whether the app is installed or not.
     *
     * @param packageName - Application package name
     * @return
     */
    private boolean isAppInstalled(String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            return false;
        }
        PackageManager pm = appContext.getContext().getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            Log.e(TAG, "isAppInstalled", e);
        }

        return installed;
    }
}
