package org.ekstep.genieproviders.telemetry;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created on 19/5/17.
 * shriharsh
 */

public abstract class AbstractTelemetryProvider extends ContentProvider {

    @Override
    public abstract boolean onCreate();

    @Override
    public abstract Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder);

    @Override
    public abstract String getType(@NonNull Uri uri);

    @Override
    public abstract Uri insert(@NonNull Uri uri, @Nullable ContentValues values);

    @Override
    public abstract int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs);

    @Override
    public abstract int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs);

    public String getCompleteUserAuthority(String authority) {
        String fullAuthorityName = String.format("%s.telemetry", authority);
        return fullAuthorityName;
    }
}
