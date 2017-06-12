package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.Map;

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
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, null, "");
        if (cursor == null) {
            Log.e(TAG, "execute: cursor is null!");
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), GetContentsTask.class.getSimpleName());
        }

        return getResponse(cursor);
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't get the content list";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/list", appQualifier);
        return Uri.parse(authority);
    }

    private GenieResponse<Map> getResponse(Cursor cursor) {
        GenieResponse<Map> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mapData = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse<Map> readCursor(Cursor cursor) {
        String serverData = cursor.getString(0);
        GenieResponse<Map> response = GsonUtil.fromJson(serverData, GenieResponse.class);
        return response;
    }
}
