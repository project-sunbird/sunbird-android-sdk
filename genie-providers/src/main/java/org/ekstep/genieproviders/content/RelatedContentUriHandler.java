package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;

import java.util.Locale;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class RelatedContentUriHandler implements IUriHandler {

    private String selection;
    private String[] selectionArgs;
    private String authority;
    private Context context;
    private GenieService genieService;

    public RelatedContentUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        // TODO: 23/5/17 Need to invoke the api from ContentService to get related contents and return
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentListUri = String.format(Locale.US, "content://%s/relatedContent", authority);

        return uri != null && contentListUri.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
