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
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Locale;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class LanguageSearchUriHandler implements IUriHandler {

    private String authority;
    private Context context;
    private String selection;
    private GenieService genieService;

    public LanguageSearchUriHandler(String authority, Context context, String queryString, GenieService genieService) {
        this.authority = authority;
        this.context = context;
        this.selection = queryString;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null) {
            cursor = getMatrixCursor();
            GenieResponse genieResponse = genieService.getLanguageService().getLanguageSearch(selection);
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
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not find the language", "Failed");
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String urlPath = String.format(Locale.US, "content://%s/langsearch", authority);
        return uri != null && urlPath.equals(uri.toString());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
