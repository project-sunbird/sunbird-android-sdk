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

/**
 * {@link GenieSDK} is the entry point and the point of contact to interact with all the services of the GenieSDK.
 * <p>
 * Initially, the application integrating GenieSDK has to initialize the sdk using "init" method by passing in the
 * {@link Context} and package name of the integrating application. And then it can call "getGenieSDK()" to access the
 * instance of GenieSDK every time, whenever needed.
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

    /**
     * This api gets the {@link UserService}, when accessed in the below way
     * <p>
     * getGenieSDK().getUserService()
     * <p><p>
     *
     * @return {@link UserService}
     */
    public UserService getUserService() {
        if (mUserService == null) {
            mUserService = new UserService(context, appQualifier);
        }

        return mUserService;
    }

    /**
     * This api gets the {@link TelemetryService}, when accessed in the below way
     * <p>
     * getGenieSDK().getTelemetryService()
     * <p><p>
     *
     * @return {@link TelemetryService}
     */
    public TelemetryService getTelemetryService() {
        if (mTelemetryService == null) {
            mTelemetryService = new TelemetryService(context, appQualifier);
        }

        return mTelemetryService;
    }

    /**
     * This api gets the {@link ContentService}, when accessed in the below way
     * <p>
     * getGenieSDK().getContentService()
     * <p><p>
     *
     * @return {@link ContentService}
     */
    public ContentService getContentService() {
        if (mContentService == null) {
            mContentService = new ContentService(context, appQualifier);
        }

        return mContentService;
    }

    /**
     * This api gets the {@link LanguageService}, when accessed in the below way
     * <p>
     * getGenieSDK().getLanguageService()
     * <p><p>
     *
     * @return {@link LanguageService}
     */
    public LanguageService getLanguageService() {
        if (mLanguageService == null) {
            mLanguageService = new LanguageService(context, appQualifier);
        }

        return mLanguageService;
    }

    /**
     * This api gets the {@link PartnerService}, when accessed in the below way
     * <p>
     * getGenieSDK().getPartnerService()
     * <p><p>
     *
     * @return {@link PartnerService}
     */
    public PartnerService getPartnerService() {
        if (mPartnerService == null) {
            mPartnerService = new PartnerService(context, appQualifier);
        }

        return mPartnerService;
    }

    /**
     * This api gets the {@link SummarizerService}, when accessed in the below way
     * <p>
     * getGenieSDK().getSummarizerService()
     * <p><p>
     *
     * @return {@link SummarizerService}
     */
    public SummarizerService getSummarizerService() {
        if (mSummarizerService == null) {
            mSummarizerService = new SummarizerService(context, appQualifier);
        }

        return mSummarizerService;
    }

}
