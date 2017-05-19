package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public abstract class AbstractContentProvider extends android.content.ContentProvider {
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

    public String getCompleteContentAuthority(String authority) {
        String fullAuthorityName = String.format("%s.languages", authority);
        return fullAuthorityName;
    }
}
