package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetAllUsersTask extends BaseTask {

    private String appQualifier;

    public GetAllUsersTask(Context context, String appQualifier) {
        super(context);
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetAllUsersTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), GetAllUsersTask.class.getSimpleName());
        }

        return getResponse(cursor);
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

    @Override
    protected String getErrorMessage() {
        return "Unable to fetch all users!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles/allUsers", appQualifier);
        return Uri.parse(authority);
    }

}
