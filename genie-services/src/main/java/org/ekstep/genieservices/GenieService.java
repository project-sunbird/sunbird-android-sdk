package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.async.GenieAsyncService;
import org.ekstep.genieservices.auth.AuthServiceImpl;
import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.ILocationInfo;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.download.DownloadServiceImpl;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.config.ConfigServiceImpl;
import org.ekstep.genieservices.content.ContentFeedbackServiceImpl;
import org.ekstep.genieservices.content.ContentServiceImpl;
import org.ekstep.genieservices.content.LanguageServiceImpl;
import org.ekstep.genieservices.notification.NotificationServiceImpl;
import org.ekstep.genieservices.partner.PartnerServiceImpl;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;
import org.ekstep.genieservices.profile.UserServiceImpl;
import org.ekstep.genieservices.profile.event.SummaryListener;
import org.ekstep.genieservices.tag.TagServiceImpl;
import org.ekstep.genieservices.telemetry.SyncServiceImpl;
import org.ekstep.genieservices.telemetry.TelemetryLogger;
import org.ekstep.genieservices.telemetry.TelemetryServiceImpl;
import org.ekstep.genieservices.utils.ContentPlayer;

/**
 * {@link GenieService} is the entry point and the point of contact to interact with all the services of the GenieService sdk.
 * <p>
 * Initially, the application integrating GenieService has to initialize the sdk using "init" method y passing in the
 * {@link Context} and package name of the integrating application. And then it can call "getService()" to access the
 * instance of GenieService every time, whenever needed.
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
    private ISummarizerService mSummarizerService;
    private IAuthService mAuthService;
    private ITagService mTagService;
    private IDownloadService mDownloadService;

    private GenieService(AppContext<Context> appContext) {
        this.mAppContext = appContext;
    }

    public static GenieService getService() {
        return sService;
    }

    public static GenieAsyncService getAsyncService() {
        return sAsyncService;
    }

    public static GenieService init(Context context, String packageName) {
        if (sService == null) {
            AppContext<Context> appContext = AndroidAppContext.buildAppContext(context, packageName);
            Logger.init(new AndroidLogger());
            ContentPlayer.init(appContext.getParams().getString(IParams.Key.APP_QUALIFIER), appContext.getParams().getString(ServiceConstants.Params.PLAYER_CONFIG));
            TelemetryLogger.init(new TelemetryServiceImpl(appContext, new UserServiceImpl(appContext)));
            //initializing event bus for Telemetry
            SummaryListener.init(appContext);
            sService = new GenieService(appContext);
        }

        sAsyncService = GenieAsyncService.init(sService);

        return sService;
    }

    /**
     * Change or update the params in runtime.
     */
    public static Void applyParams() {
        sService.mAppContext.applyParams();
        return null;
    }

    /**
     * This api gets the {@link ConfigServiceImpl}, when accessed in the below way
     * <p>
     * getService().getConfigService()
     * <p><p>
     *
     * @return {@link IConfigService}
     */
    public IConfigService getConfigService() {
        if (mConfigService == null) {
            mConfigService = new ConfigServiceImpl(mAppContext);
        }
        return mConfigService;
    }

    /**
     * This api gets the {@link UserServiceImpl}, when accessed in the below way
     * <p>
     * getService().getUserService()
     * <p><p>
     *
     * @return {@link IUserService}
     */
    public IUserService getUserService() {
        if (mUserService == null) {
            mUserService = new UserServiceImpl(mAppContext);
        }
        return mUserService;
    }

    /**
     * This api gets the {@link TelemetryServiceImpl}, when accessed in the below way
     * <p>
     * getService().getTelemetryService()
     * <p><p>
     *
     * @return {@link ITelemetryService}
     */
    public ITelemetryService getTelemetryService() {
        if (mTelemetryService == null) {
            mTelemetryService = new TelemetryServiceImpl(mAppContext, getUserService());
        }
        return mTelemetryService;
    }

    /**
     * This api gets the {@link SyncServiceImpl}, when accessed in the below way
     * <p>
     * getService().getSyncService()
     * <p><p>
     *
     * @return {@link ISyncService}
     */
    public ISyncService getSyncService() {
        if (mSyncService == null) {
            mSyncService = new SyncServiceImpl(mAppContext);
        }
        return mSyncService;
    }

    /**
     * This api gets the {@link PartnerServiceImpl}, when accessed in the below way
     * <p>
     * getService().getPartnerService()
     * <p><p>
     *
     * @return {@link IPartnerService}
     */
    public IPartnerService getPartnerService() {
        if (mPartnerService == null) {
            mPartnerService = new PartnerServiceImpl(mAppContext);
        }
        return mPartnerService;
    }

    /**
     * This api gets the {@link ContentFeedbackServiceImpl}, when accessed in the below way
     * <p>
     * getService().getContentFeedbackService()
     * <p><p>
     *
     * @return {@link IContentFeedbackService}
     */
    public IContentFeedbackService getContentFeedbackService() {
        if (mContentFeedbackService == null) {
            mContentFeedbackService = new ContentFeedbackServiceImpl(mAppContext, getUserService());
        }
        return mContentFeedbackService;
    }

    /**
     * This api gets the {@link ContentServiceImpl}, when accessed in the below way
     * <p>
     * getService().getContentService()
     * <p><p>
     *
     * @return {@link IContentService}
     */
    public IContentService getContentService() {
        if (mContentService == null) {
            mContentService = new ContentServiceImpl(mAppContext, getUserService(), getContentFeedbackService(), getConfigService(), getDownloadService());
        }
        return mContentService;
    }

    /**
     * This api gets the {@link LanguageServiceImpl}, when accessed in the below way
     * <p>
     * getService().getLanguageService()
     * <p><p>
     *
     * @return {@link ILanguageService}
     */
    public ILanguageService getLanguageService() {
        if (mLanguageService == null) {
            mLanguageService = new LanguageServiceImpl(mAppContext);
        }
        return mLanguageService;
    }

    /**
     * This api gets the {@link NotificationServiceImpl}, when accessed in the below way
     * <p>
     * getService().getNotificationService()
     * <p><p>
     *
     * @return {@link INotificationService}
     */
    public INotificationService getNotificationService() {
        if (mNotificationService == null) {
            mNotificationService = new NotificationServiceImpl(mAppContext);
        }
        return mNotificationService;
    }

    /**
     * This api gets the {@link TagServiceImpl}, when accessed in the below way
     * <p>
     * getService().getTagService()
     * <p><p>
     *
     * @return {@link ITagService}
     */
    public ITagService getTagService() {
        if (mTagService == null) {
            mTagService = new TagServiceImpl(mAppContext);
        }
        return mTagService;
    }

    /**
     * This api gets the {@link SummarizerServiceImpl}, when accessed in the below way
     * <p>
     * getService().getSummarizerService()
     * <p><p>
     *
     * @return {@link ISummarizerService}
     */
    public ISummarizerService getSummarizerService() {
        if (mSummarizerService == null) {
            mSummarizerService = new SummarizerServiceImpl(mAppContext);
        }
        return mSummarizerService;
    }

    /**
     * This api gets the {@link AuthServiceImpl}, when accessed in the below way
     * <p>
     * getService().getAuthService()
     * <p><p>
     *
     * @return {@link IAuthService}
     */
    public IAuthService getAuthService() {
        if (mAuthService == null) {
            mAuthService = new AuthServiceImpl(mAppContext);
        }
        return mAuthService;
    }

    /**
     * This api gets the {@link IKeyValueStore} set in the {@link AndroidAppContext}
     *
     * @return {@link IKeyValueStore}
     */
    public IKeyValueStore getKeyStore() {
        return mAppContext.getKeyValueStore();
    }

    /**
     * This api gets the {@link IDeviceInfo} set in the {@link AndroidAppContext}
     *
     * @return {@link IDeviceInfo}
     */
    public IDeviceInfo getDeviceInfo() {
        return mAppContext.getDeviceInfo();
    }

    /**
     * This api gets the {@link IConnectionInfo} set in the {@link AndroidAppContext}
     *
     * @return {@link IConnectionInfo}
     */
    public IConnectionInfo getConnectionInfo() {
        return mAppContext.getConnectionInfo();
    }

    /**
     * This api gets the {@link DownloadServiceImpl}, when accessed in the below way
     * <p>
     * getService().getDownloadService()
     * <p><p>
     *
     * @return {@link IDownloadService}
     */
    public IDownloadService getDownloadService() {
        if (mDownloadService == null) {
            mDownloadService = new DownloadServiceImpl(mAppContext);
        }
        return mDownloadService;
    }

    /**
     * This api gets the {@link IDownloadManager} set in the {@link AndroidAppContext}
     *
     * @return {@link IDownloadManager}
     */
    public IDownloadManager getDownloadManager() {
        return mAppContext.getDownloadManager();
    }

    /**
     * This api gets the {@link ILocationInfo} set in the {@link AndroidAppContext}
     *
     * @return {@link ILocationInfo}
     */
    public ILocationInfo getLocationInfo() {
        return mAppContext.getLocationInfo();
    }

}
