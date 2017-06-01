package org.ekstep.genieproviders.language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Locale;

public class LanguageTraversalRuleUriHandler implements IUriHandler {
    private final String TAG = LanguageTraversalRuleUriHandler.class.getSimpleName();
    private String authority;
    private Context context;
    private String selection;
    private GenieService genieService;

    public LanguageTraversalRuleUriHandler(String authority, Context context,
                                           String queryString, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.selection = queryString;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = getMatrixCursor();
        if (genieService != null && selection != null) {
            Logger.i(TAG, "Language Id - " + selection);
            GenieResponse genieResponse = genieService.getLanguageService().getLanguageTraversalRule(selection);

            if (genieResponse != null) {
                cursor.addRow(new String[]{new Gson().toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.NETWORK_ERROR,
                Constants.NO_INTERNET_CONNECTIVITY_MESSAGE, "Failed");
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String urlPath = String.format(Locale.US, "content://%s/traversalrule", authority);
        return uri != null && urlPath.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

}
