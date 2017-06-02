package org.ekstep.genieproviders.user;

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

import java.util.Locale;

/**
 * Created on 22/5/17.
 * shriharsh
 */

public class GetAllUsersUriHandler implements IUriHandler {

    private String authority;
    private GenieService genieService;

    public GetAllUsersUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null) {
            cursor = getMatrixCursor();
            GenieResponse genieResponse = genieService.getUserProfileService().getCurrentUser();

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
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not get all the profiles", "Failed");
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String currentUserUri = String.format(Locale.US, "content://%s/allUsers", authority);
        return uri != null && (currentUserUri.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
