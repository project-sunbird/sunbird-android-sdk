package org.ekstep.genieservices.commons;

/**
 * Created on 6/23/2017.
 *
 * @author anil
 */
public interface IAPKInstaller {
    /**
     * show install apk prompt, if the apk is not installed...
     *
     * @param apkFilePath APK file path
     */
    void installApk(String apkFilePath);
}
