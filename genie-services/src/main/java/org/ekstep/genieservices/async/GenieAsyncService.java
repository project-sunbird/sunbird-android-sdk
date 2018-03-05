package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;


public class GenieAsyncService {

    private static GenieService sService;

    private ConfigService mConfigService;
    private TelemetryService mTelemetryService;
    private SyncService mSyncService;
    private PartnerService mPartnerService;
    private UserService mUserService;
    private UserProfileService mUserProfileService;
    private ContentService mContentService;
    private TagService mTagService;
    private NotificationService mNotificationService;
    private SummarizerService mSummarizerService;
    private AuthService mAuthService;

    private GenieAsyncService(GenieService genieService) {
        sService = genieService;
    }

    public static GenieAsyncService init(GenieService genieService) {
        return new GenieAsyncService(genieService);
    }

    public ConfigService getConfigService() {
        if (mConfigService == null) {
            mConfigService = new ConfigService(sService);
        }
        return mConfigService;
    }

    public TelemetryService getTelemetryService() {
        if (mTelemetryService == null) {
            mTelemetryService = new TelemetryService(sService);
        }
        return mTelemetryService;
    }

    public SyncService getSyncService() {
        if (mSyncService == null) {
            mSyncService = new SyncService(sService);
        }
        return mSyncService;
    }

    public PartnerService getPartnerService() {
        if (mPartnerService == null) {
            mPartnerService = new PartnerService(sService);
        }
        return mPartnerService;
    }

    public UserService getUserService() {
        if (mUserService == null) {
            mUserService = new UserService(sService);
        }
        return mUserService;
    }

    public UserProfileService getUserProfileService() {
        if (mUserProfileService == null) {
            mUserProfileService = new UserProfileService(sService);
        }
        return mUserProfileService;
    }

    public TagService getTagService() {
        if (mTagService == null) {
            mTagService = new TagService(sService);
        }
        return mTagService;
    }

    public NotificationService getNotificationService() {
        if (mNotificationService == null) {
            mNotificationService = new NotificationService(sService);
        }
        return mNotificationService;
    }

    public ContentService getContentService() {
        if (mContentService == null) {
            mContentService = new ContentService(sService);
        }
        return mContentService;
    }

    public SummarizerService getSummarizerService() {
        if (mSummarizerService == null) {
            mSummarizerService = new SummarizerService(sService);
        }
        return mSummarizerService;
    }

    public AuthService getAuthService() {
        if (mAuthService == null) {
            mAuthService = new AuthService(sService);
        }
        return mAuthService;
    }

}
