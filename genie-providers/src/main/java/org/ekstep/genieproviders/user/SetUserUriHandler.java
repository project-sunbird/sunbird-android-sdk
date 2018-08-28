package org.ekstep.genieproviders.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.ekstep.genieproviders.IUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenericEvent;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.eventbus.EventBus;

import java.util.Locale;

/**
 * Created on 22/5/17.
 * shriharsh
 */

public class SetUserUriHandler implements IUriHandler {
    private final String TAG = SetUserUriHandler.class.getSimpleName();
    private String authority;
    private GenieService genieService;
    private String selection;

    public SetUserUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.genieService = genieService;
        this.selection = selection;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;
        if (genieService != null && selection != null) {
            Logger.i(TAG, "User Id - " + selection);
            cursor = getMatrixCursor();
            EventBus.postEvent(new GenericEvent("logout"));
            GenieResponse genieResponse = genieService.getUserService().setCurrentUser(selection);

            if (genieResponse != null) {
                cursor.addRow(new String[]{GsonUtil.toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not set the profile", "Failed");
        cursor.addRow(new String[]{GsonUtil.toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public boolean canProcess(Uri uri) {
        String currentUserUri = String.format(Locale.US, "content://%s/setUser", authority);
        return uri != null && (currentUserUri.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
}
