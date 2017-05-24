package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ekstep.genieproviders.IUriHandler;

import java.util.HashMap;
import java.util.Locale;

public class ContentUriHandler implements IUriHandler {
    private String TAG = ContentUriHandler.class.getSimpleName();
    private String authority;
    private String[] contentIdentifier;

    public ContentUriHandler(String authority, String[] contentIdentifier) {
        this.authority = authority;
        this.contentIdentifier = contentIdentifier;
    }

    @Override
    public Cursor process() {
        Cursor cursor = null;
        HashMap params = new HashMap();
        params.put("param", contentIdentifier);
        params.put("logLevel", "1");
        try {
            if (isInvalidIdentifier(params))
                return null;
//            Content content = new Content(contentIdentifier[0]);
//            //TODO:isNotLocallyPresent changes the state of object and populates from Db, need to make it explicit.
//            if (isNotLocallyPresent(content, params))
//                return null;
//            cursor = content.asCursor();
            return cursor;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return cursor;
    }

//    private boolean isNotLocallyPresent(Content content, HashMap params) {
//        if (!content.isCached(dbOperator, TAG)) {
//            apiLogger.error("Content not present locally for the identifier", METHOD_NAME, params);
//            Log.e(TAG, "Content not cached");
//            return true;
//        }
//        return false;
//    }

    private boolean isInvalidIdentifier(HashMap params) {
        if (contentIdentifier == null || contentIdentifier.length == 0) {
//            apiLogger.error("Empty content identifier for get content", METHOD_NAME, params);
            Log.e(TAG, "Empty content identifier");
            return true;
        }
        return false;
    }


    @Override
    public boolean canProcess(Uri uri) {
        String contentUri = String.format(Locale.US, "content://%s", authority);
        String contentUriWithSlash = String.format(Locale.US, "content://%s/", authority);
        return uri != null && (contentUri.equals(uri.toString()) || contentUriWithSlash.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
