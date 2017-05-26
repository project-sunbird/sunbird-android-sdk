package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.async.GenieAsyncService;
import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.config.ConfigServiceImpl;
import org.ekstep.genieservices.content.ContentFeedbackServiceImpl;
import org.ekstep.genieservices.content.ContentServiceImpl;
import org.ekstep.genieservices.content.LanguageServiceImpl;
import org.ekstep.genieservices.content.download.DownloadQueueListener;
import org.ekstep.genieservices.content.download.DownloadService;
import org.ekstep.genieservices.notification.NotificationServiceImpl;
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
    private static GenieAsyncService sAsyncService;

    private AppContext<Context> mAppContext;
    private IConfigService mConfigService;
    private ITelemetryService mTelemetryService;
    private IUserService mUserService;
    private ISyncService mSyncService;
    private IPartnerService mPartnerService;
    private IContentFeedbackService mContentFeedbackService;
    private IContentService mContentService;
    private ILanguageService mLanguageService;
    private INotificationService mNotificationService;

    private GenieService(AppContext<Context> applicationContext) {
        this.mAppContext = applicationContext;
    }

    public static GenieService getService() {
        return sService;
    }

    public static GenieAsyncService getAsyncService() {
        return GenieAsyncService.getAsyncService();
    }

    public static GenieService init(Context context, String packageName) {

        if (sService == null) {
            AppContext<Context> applicationContext = AndroidAppContext.buildAppContext(context, packageName);
            Logger.init(new AndroidLogger());
            TelemetryLogger.init(new TelemetryServiceImpl(applicationContext, new UserServiceImpl(applicationContext)));
            DownloadQueueListener.init(applicationContext);
            sService = new GenieService(applicationContext);
        }
        GenieAsyncService.init(sService);
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

    public IContentFeedbackService getContentFeedbackService() {
        if (mContentFeedbackService == null) {
            mContentFeedbackService = new ContentFeedbackServiceImpl(mAppContext);
        }
        return mContentFeedbackService;
    }

    public IContentService getContentService() {
        if (mContentService == null) {
            mContentService = new ContentServiceImpl(mAppContext, getUserProfileService(), getContentFeedbackService(), getConfigService());
        }
        return mContentService;
    }

    public ILanguageService getLanguageService() {
        if (mLanguageService == null) {
            mLanguageService = new LanguageServiceImpl(mAppContext);
        }
        return mLanguageService;
    }

    public INotificationService getNotificationService() {
        if (mNotificationService == null) {
            mNotificationService = new NotificationServiceImpl(mAppContext);
        }
        return mNotificationService;
    }

    public IKeyValueStore getKeyStore() {
        return mAppContext.getKeyValueStore();
    }

    public IDeviceInfo getDeviceInfo() {
        return mAppContext.getDeviceInfo();
    }

    public IConnectionInfo getConnectionInfo() {
        return mAppContext.getConnectionInfo();
    }

    public DownloadService getDownloadService(){return new DownloadService(mAppContext);}

}
