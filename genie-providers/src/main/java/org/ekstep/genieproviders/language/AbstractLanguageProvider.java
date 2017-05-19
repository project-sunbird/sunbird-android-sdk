package org.ekstep.genieproviders.language;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public abstract class AbstractLanguageProvider extends ContentProvider {

    @Override
    public abstract boolean onCreate();

    @Override
    public abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    @Override
    public abstract String getType(Uri uri);

    @Override
    public abstract Uri insert(Uri uri, ContentValues values);

    @Override
    public abstract int delete(Uri uri, String selection, String[] selectionArgs);

    @Override
    public abstract int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);

    public String getCompleteLanguageAuthority(String authority) {
        String fullAuthorityName = String.format("%s.languages", authority);
        return fullAuthorityName;
    }
}
