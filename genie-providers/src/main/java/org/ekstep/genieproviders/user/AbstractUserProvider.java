package org.ekstep.genieproviders.user;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;

/**
 * Created on 19/5/17.
 * shriharsh
 */

public abstract class AbstractUserProvider extends BaseContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: 22/5/17 Need to add diffent service for getting all the users
//        Cursor cursor = database.rawQuery(String.format(Locale.US, "select * from %s order by %s asc", ProfileEntry.TABLE_NAME, ProfileEntry.COLUMN_NAME_HANDLE), null);
//        Log.i(LOG_TAG, "Read profiles, count:" + String.valueOf(cursor.getCount()));
//        return cursor;
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.profiles", getPackageName());
    }

    @Override
    public abstract Uri insert(@NonNull Uri uri, @Nullable ContentValues values);

    @Override
    public abstract int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs);

    @Override
    public abstract int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs);

    public String getCompleteUserAuthority(String authority) {
        String fullAuthorityName = String.format("%s.profiles", authority);
        return fullAuthorityName;
    }

    public abstract String getPackageName();

    @Override
    public String getPackage() {
        return getPackageName();
    }
}
