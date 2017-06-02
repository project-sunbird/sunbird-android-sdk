package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetContentsTask extends BaseTask {

    private final String TAG = GetContentsTask.class.getSimpleName();
    private String appQualifier;

    public GetContentsTask(Context context, String appQualifier) {
        super(context);
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetContentsTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, null, "");
        if (cursor == null) {
            Log.e(TAG, "execute: cursor is null!");
            String logMessage = "Couldn't get the content list";
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), logMessage);
        }
        GenieResponse genieResponse = getResponse(cursor);
        return genieResponse;
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't get the content list";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/list", appQualifier);
        return Uri.parse(authority);
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
}
