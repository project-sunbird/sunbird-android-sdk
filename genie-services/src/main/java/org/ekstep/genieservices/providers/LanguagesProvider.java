package org.ekstep.genieservices.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.ekstep.genieproviders.IHandleUri;
import org.ekstep.genieproviders.language.AbstractLanguageProvider;
import org.ekstep.genieproviders.language.LanguageTraversalRuleUriHandler;
import org.ekstep.genieproviders.language.LanguageUriHandlerFactory;

import java.util.List;


public class LanguagesProvider extends AbstractLanguageProvider {
    private final String AUTHORITY = "org.ekstep.genieservices";

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
        return LanguageUriHandlerFactory.uriHandlers(getCompleteLanguageAuthority(AUTHORITY),
                getContext(), selection);
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.languages", AUTHORITY);
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

}
