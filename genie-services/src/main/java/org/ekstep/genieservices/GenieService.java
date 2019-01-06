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
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.download.DownloadServiceImpl;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.config.ConfigServiceImpl;
import org.ekstep.genieservices.config.DialCodeServiceImpl;
import org.ekstep.genieservices.config.FormServiceImpl;
import org.ekstep.genieservices.config.FrameworkServiceImpl;
import org.ekstep.genieservices.content.ContentFeedbackServiceImpl;
import org.ekstep.genieservices.content.ContentServiceImpl;
import org.ekstep.genieservices.content.CourseServiceImpl;
import org.ekstep.genieservices.content.LanguageServiceImpl;
import org.ekstep.genieservices.event.SummaryListener;
import org.ekstep.genieservices.group.GroupServiceImpl;
import org.ekstep.genieservices.notification.AnnouncementServiceImpl;
import org.ekstep.genieservices.notification.NotificationServiceImpl;
import org.ekstep.genieservices.page.PageServiceImpl;
import org.ekstep.genieservices.partner.PartnerServiceImpl;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;
import org.ekstep.genieservices.profile.UserProfileServiceImpl;
import org.ekstep.genieservices.profile.UserServiceImpl;
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
    private IUserProfileService mUserProfileService;
    private IGroupService mGroupService;
    private ICourseService mCourseService;
    private ISyncService mSyncService;
    private IPartnerService mPartnerService;
    private IContentFeedbackService mContentFeedbackService;
    private IContentService mContentService;
    private ILanguageService mLanguageService;
    private INotificationService mNotificationService;
    private ISummarizerService mSummarizerService;
    private IAuthService mAuthService;
    private IFrameworkService mFrameworkService;
    private ITagService mTagService;
    private IDownloadService mDownloadService;
    private IAuthSession<Session> mAuthSession;
    private IPageService mPageService;
    private IAnnouncementService mAnnouncementService;
    private IFormService mFormService;
    private IDialCodeService mDialCodeService;

    private GenieService(AppContext<Context> appContext) {
        this.mAppContext = appContext;
    }

    public static GenieService getService() {
        return sService;
    }

    public static GenieAsyncService getAsyncService() {
        return sAsyncService;
    }

    /**
     * Initialize the SDK.
     *
     * @param context     {@link Context}
     * @param packageName package name of the integrating application which is defined in Manifest.xml
     * @return instance of {@link GenieService}.
     */
    public static GenieService init(Context context, String packageName) {
        if (sService == null) {
            AppContext<Context> appContext = AndroidAppContext.buildAppContext(context, packageName);
            Logger.init(new AndroidLogger());

            TelemetryLogger.init(new TelemetryServiceImpl(appContext, new UserServiceImpl(appContext, new UserProfileServiceImpl(appContext, null)), new GroupServiceImpl(appContext)));
            //initializing event bus for Telemetry
            SummaryListener.init(appContext);
            sService = new GenieService(appContext);
            ContentPlayer.init(appContext);

            //wipe the details of course context
            appContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SUNBIRD_CONTENT_CONTEXT, "");
        }

        sAsyncService = GenieAsyncService.init(sService);

        return sService;
    }

    /**
     * After the GenieService initialization in client app wants to change or update the params in runtime then call this method with updated params.
     *
     * @param params {@link IParams}
     */
    public static Void setParams(IParams params) {
        sService.mAppContext.applyParams(params);
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
            mUserService = new UserServiceImpl(mAppContext, getUserProfileService());
        }
        return mUserService;
    }

    /**
     * This api gets the {@link UserProfileServiceImpl}, when accessed in the below way
     * <p>
     * getService().getUserProfileService()
     * <p>
     *
     * @return {@link IUserProfileService}
     */
    public IUserProfileService getUserProfileService() {
        if (mUserProfileService == null) {
            mUserProfileService = new UserProfileServiceImpl(mAppContext, getAuthSession());
        }

        return mUserProfileService;
    }

    /**
     * This api gets the {@link GroupServiceImpl}, when accessed in the below way
     * <p>
     * getService().getGroupService()
     * <p><p>
     *
     * @return {@link IGroupService}
     */
    public IGroupService getGroupService() {
        if (mGroupService == null) {
            mGroupService = new GroupServiceImpl(mAppContext);
        }
        return mGroupService;
    }

    /**
     * This api gets the {@link CourseServiceImpl}, when accessed in the below way
     * <p>
     * getService().getCourseService()
     * <p><p>
     *
     * @return {@link ICourseService}
     */
    public ICourseService getCourseService() {
        if (mCourseService == null) {
            mCourseService = new CourseServiceImpl(mAppContext, getAuthSession(), getUserProfileService(), getContentService());
        }

        return mCourseService;
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
            mTelemetryService = new TelemetryServiceImpl(mAppContext, getUserService(), getGroupService());
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
            mSyncService = new SyncServiceImpl(mAppContext, getTelemetryService());
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
            mContentService = new ContentServiceImpl(mAppContext, getUserService(), getContentFeedbackService(),
                    getConfigService(), getDownloadService(), getAuthSession());
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
     * This api gets the {@link AnnouncementServiceImpl}, when accessed in the below way
     * <p>
     * getService().getAnnouncementService()
     * <p><p>
     *
     * @return {@link IAnnouncementService}
     */
    public IAnnouncementService getAnnouncementService() {
        if (mAnnouncementService == null) {
            mAnnouncementService = new AnnouncementServiceImpl(mAppContext, getAuthSession());
        }
        return mAnnouncementService;
    }

    /**
     * This api gets the {@link org.ekstep.genieservices.config.FrameworkServiceImpl}, when accessed in the below way
     * <p>
     * getService().getFrameworkService()
     * <p><p>
     *
     * @return {@link IFrameworkService}
     */
    public IFrameworkService getFrameworkService() {
        if (mFrameworkService == null) {
            mFrameworkService = new FrameworkServiceImpl(mAppContext);
        }
        return mFrameworkService;
    }

    public IPageService getPageService() {
        if (mPageService == null) {
            mPageService = new PageServiceImpl(mAppContext);
        }
        return mPageService;
    }

    public IFormService getFormService() {
        if (mFormService == null) {
            mFormService = new FormServiceImpl(mAppContext);
        }
        return mFormService;
    }

    public IDialCodeService getDialCodeService() {
        if (mDialCodeService == null) {
            mDialCodeService = new DialCodeServiceImpl(mAppContext);
        }
        return mDialCodeService;
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

    public IAuthSession<Session> getAuthSession() {
        if (mAuthSession == null) {
            String authSessionClass = mAppContext.getParams().getString(ServiceConstants.Params.OAUTH_SESSION);
            if (authSessionClass != null) {
                Class<?> classInstance = ReflectionUtil.getClass(authSessionClass);
                if (classInstance != null) {
                    mAuthSession = (IAuthSession) ReflectionUtil.getInstance(classInstance);
                }
                mAuthSession.initAuth(mAppContext);
            }
        }
        return mAuthSession;
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
