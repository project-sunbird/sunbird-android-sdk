package org.ekstep.genieproviders.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Locale;

/**
 * Created on 22/5/17.
 * shriharsh
 */

public class CurrentUserUriHandler implements IUriHandler {

    private String authority;
    private GenieService genieService;

    public CurrentUserUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        if (genieService != null) {
            genieService.getUserProfileService().getCurrentUser();

            // TODO: 22/5/17 this should retrun Cursor but not null
            return null;
        }
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String currentUserUri = String.format(Locale.US, "content://%s.profiles/currentUser", authority);
        return uri != null && (currentUserUri.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
