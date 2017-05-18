package org.ekstep.genieproviders.language;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.ekstep.genieproviders.IHandleUri;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Locale;

public class LanguageTraversalRuleUriHandler implements IHandleUri {
    private static final String TAG = LanguageTraversalRuleUriHandler.class.getSimpleName();
    private String authority;
    private Context context;
    private String selection;

    public LanguageTraversalRuleUriHandler(String authority, Context context,
                                           String queryString) {
        this.authority = authority;
        this.context = context;
        this.selection = queryString;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = getMatrixCursor();
//        IConnectionInfo connectionInfo = appContext.getConnectionInfo();
        // TODO: 16/5/17 TraversalRuleAPI response should be got from Service and the null should be replaced
        GenieResponse genieResponse = null;

        if (genieResponse != null) {
            cursor.addRow(new String[]{new Gson().toJson(genieResponse)});
        } else {
            getErrorResponse(cursor);
        }

        return cursor;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ProviderConstants.NETWORK_ERROR,
                ProviderConstants.NO_INTERNET_CONNECTIVITY_MESSAGE, "Failed");
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
