package org.ekstep.genieproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.utils.Logger;

/**
 * Created on 22/5/17.
 * shriharsh
 */

public abstract class BaseContentProvider extends ContentProvider {

    private GenieService mGenieService;

    public GenieService getService() {
        mGenieService = GenieService.getService();

        if (mGenieService == null) {
            mGenieService = GenieService.init(getContext(), getPackageName());
            Logger.i("BaseContentProvider", "GenieService is null!");
        }

        return mGenieService;
    }

    public abstract String getPackageName();

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
