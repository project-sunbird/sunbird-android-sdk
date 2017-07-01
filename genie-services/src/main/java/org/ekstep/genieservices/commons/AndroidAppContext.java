package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.cache.PreferenceWrapper;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.download.AndroidDownloadManager;
import org.ekstep.genieservices.commons.network.AndroidHttpClientFactory;
import org.ekstep.genieservices.commons.network.AndroidNetworkConnectivity;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.network.IHttpClientFactory;

/**
 * Created on 18/4/17.
 */
public class AndroidAppContext extends AppContext<Context> {

    private static final String SHARED_PREFERENCE_NAME = "org.ekstep.genieservices.preference_file";

    private IParams mParams;
    private IDBSession mDBSession;
    private IConnectionInfo mConnectionInfo;
    private IHttpClientFactory mHttpClientFactory;
    private IKeyValueStore mKeyValueOperation;
    private IDeviceInfo mDeviceInfo;
    private ILocationInfo mLocationInfo;
    private IDownloadManager mDownloadManager;
    private IAPKInstaller mAPKInstaller;

    private AndroidAppContext(Context context) {
        super(context);
    }

    public static AppContext<Context> buildAppContext(Context context, String appPackage) {
        AndroidAppContext appContext = new AndroidAppContext(context);
        appContext.setParams(new BuildParams(context, appPackage));
        appContext.setDBSession(ServiceDbHelper.getGSDBSession(appContext));
        appContext.setConnectionInfo(new AndroidNetworkConnectivity(appContext));
        appContext.setHttpClientFactory(new AndroidHttpClientFactory(appContext));
        appContext.setKeyValueStore(new PreferenceWrapper(context, SHARED_PREFERENCE_NAME));
        appContext.setDeviceInfo(new DeviceInfo(context));
        appContext.setLocationInfo(new LocationInfo(context));
        appContext.setDownloadManager(new AndroidDownloadManager(context));
        appContext.setAPKInstaller(new APKInstaller(appContext));
        return appContext;
    }

    @Override
    public IParams getParams() {
        return mParams;
    }

    private void setParams(IParams params) {
        this.mParams = params;
    }

    @Override
    public ILocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    private void setLocationInfo(ILocationInfo mLocationInfo) {
        this.mLocationInfo = mLocationInfo;
    }

    @Override
    public IDBSession getDBSession() {
        return mDBSession;
    }

    @Override
    public Void setDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

    @Override
    public IKeyValueStore getKeyValueStore() {
        return mKeyValueOperation;
    }

    private void setKeyValueStore(IKeyValueStore keyValueOperation) {
        this.mKeyValueOperation = keyValueOperation;
    }

    @Override
    public IConnectionInfo getConnectionInfo() {
        return mConnectionInfo;
    }

    private void setConnectionInfo(IConnectionInfo connectionInfo) {
        this.mConnectionInfo = connectionInfo;
    }

    @Override
    public IHttpClientFactory getHttpClientFactory() {
        return mHttpClientFactory;
    }

    private void setHttpClientFactory(IHttpClientFactory clientFactory) {
        this.mHttpClientFactory = clientFactory;
    }

    @Override
    public IDeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    private void setDeviceInfo(IDeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
    }

    @Override
    public IDownloadManager getDownloadManager() {
        return mDownloadManager;
    }

    private void setDownloadManager(IDownloadManager downloadManager) {
        this.mDownloadManager = downloadManager;
    }

    @Override
    public IAPKInstaller getAPKInstaller() {
        return mAPKInstaller;
    }

    private void setAPKInstaller(APKInstaller APKInstaller) {
        this.mAPKInstaller = APKInstaller;
    }
}
