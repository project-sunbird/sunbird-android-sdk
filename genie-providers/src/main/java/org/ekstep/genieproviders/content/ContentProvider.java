package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

public class ContentProvider extends android.content.ContentProvider {
    private final String AUTHORITY = "org.ekstep.genieservices.content";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<ContentUriHandler> handlers = ContentUriHandlerFactory.uriHandlers(AUTHORITY, getContext(), selection, selectionArgs);
        for (IHandleUri handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.item/org.ekstep.genieservices.provider.content";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        List<ContentUriHandler> handlers = ContentUriHandlerFactory.uriHandlers(AUTHORITY, getContext(), null, null);
        for (IHandleUri handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.insert(uri, values);
            }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
