package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IUriHandler;

import java.util.List;

public abstract class AbstractContentProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<IUriHandler> handlers = ContentUriHandlerFactory.uriHandlers(getCompletePath(), getContext(), selection, selectionArgs);
        for (IUriHandler handler : handlers) {
            if (handler.canProcess(uri)) {
                return handler.process();
            }
        }

        return null;
    }


    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.content", getPackageName());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        FeedbackUriHandler feedbackUriHandler = new FeedbackUriHandler(getCompletePath(), getContext(), null, null);
        Uri responseUri = feedbackUriHandler.insert(uri, values);

        if (responseUri != null) {
            return responseUri;
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

    private String getCompletePath() {
        String CONTENT_PATH = "content";
        return String.format("%s.%s", getPackageName(), CONTENT_PATH);
    }

    public abstract String getPackageName();

    @Override
    public String getPackage() {
        return getPackageName();
    }
}
