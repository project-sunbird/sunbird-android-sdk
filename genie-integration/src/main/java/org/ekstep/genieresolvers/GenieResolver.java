package org.ekstep.genieresolvers;

import android.content.Context;

import org.ekstep.genieresolvers.content.GetContentTask;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public class GenieResolver {

    private static GenieResolver sGenieResolver;
    private String appQualifier;
    private Context context;
    private GetContentTask mGetContentTask;

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

    public GetContentTask getContentTask(String contentId) {
        if (mGetContentTask == null) {
            mGetContentTask = new GetContentTask(context, appQualifier, contentId);
        }
        return mGetContentTask;
    }

}
