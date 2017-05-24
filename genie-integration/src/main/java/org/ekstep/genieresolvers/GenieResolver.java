package org.ekstep.genieresolvers;

import android.content.Context;

import org.ekstep.genieresolvers.content.ContentService;
import org.ekstep.genieresolvers.language.LanguageService;
import org.ekstep.genieresolvers.telemetry.TelemetryService;
import org.ekstep.genieresolvers.user.UserService;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public class GenieResolver {

    private static GenieResolver sGenieResolver;
    private String appQualifier;
    private Context context;
    private UserService mUserService;
    private TelemetryService mTelemetryService;
    private ContentService mContentService;
    private LanguageService mLanguageService;

    private GenieResolver(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public static GenieResolver getGenieResolver() {
        return sGenieResolver;
    }

    public static GenieResolver init(Context context, String authorityName) {

        if (sGenieResolver == null) {
            sGenieResolver = new GenieResolver(context, authorityName);
        }
        return sGenieResolver;
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


}
