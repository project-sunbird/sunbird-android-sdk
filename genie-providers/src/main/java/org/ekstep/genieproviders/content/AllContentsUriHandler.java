package org.ekstep.genieproviders.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Locale;

public class AllContentsUriHandler implements IUriHandler {
    private String authority;
    private String[] contentIdentifier;
    private GenieService genieService;

    public AllContentsUriHandler(String authority, String[] contentIdentifier, GenieService genieService) {
        this.authority = authority;
        this.contentIdentifier = contentIdentifier;
        this.genieService = genieService;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null) {
            cursor = getMatrixCursor();
            GenieResponse genieResponse = genieService.getContentService().getAllLocalContent(null);

            if (genieResponse != null && genieResponse.getStatus()) {
                cursor.addRow(new String[]{GsonUtil.toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not fetch all contents", AllContentsUriHandler.class.getSimpleName());
        cursor.addRow(new String[]{GsonUtil.toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String contentUriWithSlash = String.format(Locale.US, "content://%s/list", authority);
        return uri != null && (contentUriWithSlash.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
