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
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileRequest;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.List;
import java.util.Locale;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public class GetAllProfilesUriHandler implements IUriHandler {
    private final String TAG = GetAllProfilesUriHandler.class.getSimpleName();
    private String authority;
    private GenieService genieService;
    private String selection;


    public GetAllProfilesUriHandler(String authority, Context context, String selection, String[] selectionArgs, GenieService genieService) {
        this.authority = authority;
        this.genieService = genieService;
        this.selection = selection;
    }

    @Override
    public Cursor process() {
        MatrixCursor cursor = null;

        if (genieService != null && selection != null) {
            cursor = getMatrixCursor();

            Logger.i(TAG, "Selection data - " + selection);

            ProfileRequest.Builder profileRequest = GsonUtil.fromJson(selection, ProfileRequest.Builder.class);

            GenieResponse<List<Profile>> genieResponse = null;

            try {
                genieResponse = genieService.getUserService().getAllUserProfile(profileRequest.build());
            } catch (Exception e) {
                Logger.e(TAG, "Error - " + e.getMessage());
            }

            if (genieResponse != null) {
                cursor.addRow(new String[]{GsonUtil.toJson(genieResponse)});
            } else {
                getErrorResponse(cursor);
            }
        }

        return cursor;
    }

    @Override
    public boolean canProcess(Uri uri) {
        String currentUserUri = String.format(Locale.US, "content://%s/allProfiles", authority);
        return uri != null && (currentUserUri.equals(uri.toString()));
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @NonNull
    protected GenieResponse getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not get all the profiles", "Failed");
        cursor.addRow(new String[]{GsonUtil.toJson(errorResponse)});
        return errorResponse;
    }

    @NonNull
    protected MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }
}
