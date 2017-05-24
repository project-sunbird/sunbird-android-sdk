package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.config.ConfigServiceImpl;
import org.ekstep.genieservices.partner.PartnerServiceImpl;
import org.ekstep.genieservices.profile.UserServiceImpl;
import org.ekstep.genieservices.telemetry.SyncServiceImpl;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.TelemetryServiceImpl;

/**
 * Created on 4/14/2017.
 *
 * @author anil
 */
public class GenieService {

    private static GenieService sService;

    private AppContext<Context> mAppContext;
    private IConfigService mConfigService;
    private ITelemetryService mTelemetryService;
    private IUserService mUserService;
    private ISyncService mSyncService;
    private IPartnerService mPartnerService;

    private GenieService(AppContext<Context> applicationContext) {
        this.mAppContext = applicationContext;
    }

    public static GenieService getService() {
        return sService;
    }

    public static GenieService init(Context context, String packageName, String apiKey, String gDataId) {

        if (sService == null) {
            AppContext<Context> applicationContext = AndroidAppContext.buildAppContext(context, packageName, apiKey, gDataId);
            Logger.init(new AndroidLogger());
            TelemetryLogger.init(new TelemetryServiceImpl(applicationContext, new UserServiceImpl(applicationContext)));
            sService = new GenieService(applicationContext);
        }

        //TODO init the async service here
        return sService;
    }

    public IConfigService getConfigService() {
        if (mConfigService == null) {
            mConfigService = new ConfigServiceImpl(mAppContext);
        }
        return mConfigService;
    }

    public IUserService getUserProfileService() {
        if (mUserService == null) {
            mUserService = new UserServiceImpl(mAppContext);
        }
        return mUserService;
    }

    public ITelemetryService getTelemetryService() {
        if (mTelemetryService == null) {
            mTelemetryService = new TelemetryServiceImpl(mAppContext, getUserProfileService());
        }
        return mTelemetryService;
    }

    public ISyncService getSyncService() {
        if (mSyncService == null) {
            mSyncService = new SyncServiceImpl(mAppContext);
        }
        return mSyncService;
    }

    public IPartnerService getPartnerService() {
        if (mPartnerService == null) {
            mPartnerService = new PartnerServiceImpl(mAppContext);
        }
        return mPartnerService;
    }
}
