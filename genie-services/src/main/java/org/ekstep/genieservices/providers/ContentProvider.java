package org.ekstep.genieservices.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.IHandleUri;
import org.ekstep.genieproviders.content.AbstractContentProvider;
import org.ekstep.genieproviders.content.ContentUriHandler;
import org.ekstep.genieproviders.content.ContentUriHandlerFactory;

import java.util.List;


/**
 * Created on 18/5/17.
 * shriharsh
 */

public class ContentProvider extends AbstractContentProvider {

    private String AUTHORITY = "org.ekstep.genieservices";

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<ContentUriHandler> handlers = ContentUriHandlerFactory.uriHandlers(getCompleteContentAuthority(AUTHORITY), getContext(), selection, selectionArgs);
        for (IHandleUri handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.content", AUTHORITY);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        List<ContentUriHandler> handlers = ContentUriHandlerFactory.uriHandlers(getCompleteContentAuthority(AUTHORITY), getContext(), null, null);
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
