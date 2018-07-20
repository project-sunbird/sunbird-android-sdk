package org.ekstep.genieproviders.group;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IUriHandler;

import java.util.List;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public abstract class AbstractGroupProvider extends BaseContentProvider{

    private final String TAG = AbstractGroupProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        List<IUriHandler> handlers = GroupUriHandlerFactory.uriHandlers(getCompletePath(), getContext(), selection, selectionArgs, getService());
        for (IUriHandler handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.groups", getPackageName());

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

    private String getCompletePath() {
        String GROUP_PATH = "groups";
        return String.format("%s.%s", getPackageName(), GROUP_PATH);
    }
}
