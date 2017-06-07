package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;

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
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), GetAllUsersTask.class.getSimpleName());
        }

        GenieResponse genieResponse = getResponse(cursor);
        return genieResponse;
    }

    private GenieResponse<List<Profile>> getResponse(Cursor cursor) {
        GenieResponse<List<Profile>> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mapData = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse<List<Profile>> readCursor(Cursor cursor) {
        String serverData = cursor.getString(0);
        Type type = new TypeToken<GenieResponse<List<Profile>>>() {
        }.getType();
        GenieResponse<List<Profile>> response = GsonUtil.fromJson(serverData, type);
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
