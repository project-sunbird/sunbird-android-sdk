package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class SetUserTask extends BaseTask {

    private final String TAG = SetUserTask.class.getSimpleName();
    private String appQualifier;
    private String userId;

    public SetUserTask(Context context, String appQualifier, String userId) {
        super(context);
        this.appQualifier = appQualifier;
        this.userId = userId;
    }

    @Override
    protected String getLogTag() {
        return SetUserTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, userId, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), TAG);
        }

        GenieResponse genieResponse = getResponse(cursor);
        return genieResponse;
    }

    private GenieResponse<String> getResponse(Cursor cursor) {
        GenieResponse<String> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mapData = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse<String> readCursor(Cursor cursor) {
        Gson gson = new Gson();
        String serverData = cursor.getString(0);
        GenieResponse<String> response = gson.fromJson(serverData, GenieResponse.class);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Could not set user!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles/setUser", appQualifier);
        return Uri.parse(authority);
    }

}
