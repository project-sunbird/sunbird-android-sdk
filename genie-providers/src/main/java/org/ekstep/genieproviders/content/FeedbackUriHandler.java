package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.ekstep.genieproviders.IUriHandler;
import java.util.Locale;

public class FeedbackUriHandler implements IUriHandler {
    private String authority;
    private Context context;

    public FeedbackUriHandler(String authority, Context context, String selection, String[] selectionArgs) {
        this(authority, context);
    }

    public FeedbackUriHandler(String authority, Context context) {
        this.authority = authority;
        this.context = context;
    }

    @Override
    public Cursor process() {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: 23/5/17 Need to invoke from FeedbackContentService to save feedback and also generate the event
        return null;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentFeedbackUri = String.format(Locale.US, "content://%s/feedback", authority);
        return uri != null && contentFeedbackUri.equals(uri.toString());
    }

}
