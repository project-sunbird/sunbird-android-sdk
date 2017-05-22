package org.ekstep.genieproviders.language;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.IHandleUri;

import java.util.List;

/**
 * Created on 18/5/17.
 * shriharsh
 */

public abstract class AbstractLanguageProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<LanguageTraversalRuleUriHandler> handlers = getUriHandlers(selection, selectionArgs);
        for (IHandleUri handler : handlers)
            if (handler.canProcess(uri))
                return handler.process();
        return null;
    }

    @NonNull
    protected List<LanguageTraversalRuleUriHandler> getUriHandlers(String selection, String[] selectionArgs) {
        return LanguageUriHandlerFactory.uriHandlers(getCompletePath(),
                getContext(), selection);
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.languages", getPackageName());
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
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
        String LANGUAGE_PATH = "languages";
        return String.format("%s.%s", getPackageName(), LANGUAGE_PATH);
    }

    public abstract String getPackageName();

    @Override
    public String getPackage() {
        return getPackageName();
    }
}
