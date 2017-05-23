package org.ekstep.genieproviders.language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.IUriHandler;

import java.util.Locale;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetAllLanguagesUriHandler implements IUriHandler {

    private String authority;
    private Context context;
    private String selection;


    public GetAllLanguagesUriHandler(String authority, Context context, String queryString) {
        this.authority = authority;
        this.context = context;
        this.selection = queryString;
    }

    @Override
    public Cursor process() {
        // TODO: 23/5/17 Need to invoke method from Language Service API and send back the required response
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String urlPath = String.format(Locale.US, "content://%s/all", authority);
        return uri != null && urlPath.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
