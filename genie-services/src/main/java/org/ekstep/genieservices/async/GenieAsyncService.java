package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;


public class GenieAsyncService {

    private static GenieService sService;
    private static GenieAsyncService sGenieAsyncService;
    private static ConfigService sConfigService;
    private static SyncService sSyncService;
    private static TelemetryService sTelemetryService;
    private static UserService sUserService;
    private static ContentService sContentService;
    private static TagService sTagService;
    private static NotificationService sNotificationService;
    private static SummarizerService sSummarizerService;

    public static GenieAsyncService getAsyncService() {
        if (sGenieAsyncService == null) {
            sGenieAsyncService = new GenieAsyncService();
        }
        return sGenieAsyncService;
    }

    public static void init(GenieService genieService) {
        sService = genieService;
        sGenieAsyncService = new GenieAsyncService();
    }

    public ConfigService getConfigService() {
        if (sConfigService == null) {
            sConfigService = new ConfigService(sService);
        }
        return sConfigService;
    }

    public UserService getUserService() {
        if (sUserService == null) {
            sUserService = new UserService(sService);
        }
        return sUserService;
    }

    public SyncService getSyncService() {
        if (sSyncService == null) {
            sSyncService = new SyncService(sService);
        }
        return sSyncService;
    }

    public TelemetryService getTelemetryService() {
        if (sTelemetryService == null) {
            sTelemetryService = new TelemetryService(sService);
        }
        return sTelemetryService;
    }

    public TagService getTagService() {
        if (sTagService == null) {
            sTagService = new TagService(sService);
        }
        return sTagService;
    }

    public NotificationService getNotificationService() {
        if (sNotificationService == null) {
            sNotificationService = new NotificationService(sService);
        }
        return sNotificationService;
    }

    public ContentService getContentService() {
        if (sContentService == null) {
            sContentService = new ContentService(sService);
        }
        return sContentService;
    }

    public SummarizerService getSummarizerService() {
        if (sSummarizerService == null) {
            sSummarizerService = new SummarizerService(sService);
        }
        return sSummarizerService;
    }

}
