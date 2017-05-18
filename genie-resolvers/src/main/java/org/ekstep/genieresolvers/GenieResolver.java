package org.ekstep.genieresolvers;

import android.content.Context;

import org.ekstep.genieresolvers.content.GenieContentResolver;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public class GenieResolver {

    private static GenieResolver sGenieResolver;
    private String authorityName;
    private Context context;
    private GenieContentResolver mGenieContentResolver;

    private GenieResolver(Context context, String authorityName) {
        this.context = context;
        this.authorityName = authorityName;
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

    public GenieContentResolver getGenieContentResolver(String contentId) {
        if (mGenieContentResolver == null) {
            mGenieContentResolver = new GenieContentResolver(context, authorityName, contentId);
        }
        return mGenieContentResolver;
    }

}
