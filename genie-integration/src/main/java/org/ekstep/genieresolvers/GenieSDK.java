package org.ekstep.genieresolvers;

import android.content.Context;

import org.ekstep.genieresolvers.content.ContentService;
import org.ekstep.genieresolvers.language.LanguageService;
import org.ekstep.genieresolvers.partner.PartnerService;
import org.ekstep.genieresolvers.summarizer.SummarizerService;
import org.ekstep.genieresolvers.telemetry.TelemetryService;
import org.ekstep.genieresolvers.user.UserService;

/**
 * This is the entry point of the GenieSDK
 */
public class GenieSDK {

    private static GenieSDK sGenieSDK;
    private String appQualifier;
    private Context context;
    private UserService mUserService;
    private TelemetryService mTelemetryService;
    private ContentService mContentService;
    private LanguageService mLanguageService;
    private PartnerService mPartnerService;
    private SummarizerService mSummarizerService;

    private GenieSDK(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public static GenieSDK getGenieSDK() {
        return sGenieSDK;
    }

    public static GenieSDK init(Context context, String authorityName) {

        if (sGenieSDK == null) {
            sGenieSDK = new GenieSDK(context, authorityName);
        }
        return sGenieSDK;
    }

    public UserService getUserService() {
        if (mUserService == null) {
            mUserService = new UserService(context, appQualifier);
        }

        return mUserService;
    }

    public TelemetryService getTelemetryService() {
        if (mTelemetryService == null) {
            mTelemetryService = new TelemetryService(context, appQualifier);
        }

        return mTelemetryService;
    }

    public ContentService getContentService() {
        if (mContentService == null) {
            mContentService = new ContentService(context, appQualifier);
        }

        return mContentService;
    }

    public LanguageService getLanguageService() {
        if (mLanguageService == null) {
            mLanguageService = new LanguageService(context, appQualifier);
        }

        return mLanguageService;
    }

    public PartnerService getPartnerService() {
        if (mPartnerService == null) {
            mPartnerService = new PartnerService(context, appQualifier);
        }

        return mPartnerService;
    }

    public SummarizerService getSummarizerService() {
        if (mSummarizerService == null) {
            mSummarizerService = new SummarizerService(context, appQualifier);
        }

        return mSummarizerService;
    }

}
