package org.ekstep.genieproviders.language;

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

public class LanguageSearchUriHandler implements IUriHandler {

    private String authority;
    private Context context;
    private String selection;
    private GenieService genieService;

    public LanguageSearchUriHandler(String authority, Context context, String queryString, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.selection = queryString;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        // TODO: 23/5/17 Need to invoke method from Language Service API and send back the required response
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String urlPath = String.format(Locale.US, "content://%s/langsearch", authority);
        return uri != null && urlPath.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
