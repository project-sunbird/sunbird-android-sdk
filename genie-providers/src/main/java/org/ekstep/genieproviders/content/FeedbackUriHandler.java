package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateProfile;

import java.util.Locale;

public class FeedbackUriHandler implements IUriHandler {
    private String authority;
    private Context context;
    private GenieService genieService;

    public FeedbackUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this(authority, context, genieService);
    }

    public FeedbackUriHandler(String authority, Context context, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.genieService = genieService;
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
