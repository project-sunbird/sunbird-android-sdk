package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Locale;

public class ContentUriHandler implements IUriHandler {
    private final String TAG = ContentUriHandler.class.getSimpleName();
    private String authority;
    private String[] contentIdentifier;
    private GenieService genieService;

    public ContentUriHandler(String authority, String[] contentIdentifier, GenieService genieService) {
        this.authority = authority;
        this.contentIdentifier = contentIdentifier;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null && contentIdentifier[0] != null) {
            cursor = getMatrixCursor();
            Logger.i(TAG, "Content Identifier - " + contentIdentifier[0]);
            ContentDetailsRequest.Builder builder = new ContentDetailsRequest.Builder();
            builder.forContent(contentIdentifier[0]);
            GenieResponse genieResponse = genieService.getContentService().getContentDetails(builder.build());

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
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not find the content", "Failed");
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentUri = String.format(Locale.US, "content://%s", authority);
        String contentUriWithSlash = String.format(Locale.US, "content://%s/", authority);
        return uri != null && (contentUri.equals(uri.toString()) || contentUriWithSlash.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
