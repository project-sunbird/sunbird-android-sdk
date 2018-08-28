package org.ekstep.genieresolvers.user;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Map;

/**
 * Created on 20/7/18.
 * shriharsh
 */
public class GetAllProfilesTask extends BaseTask {

    private String appQualifier;
    private String requestData;


    public GetAllProfilesTask(Context context, String appQualifier, String requestData) {
        super(context);
        this.appQualifier = appQualifier;
        this.requestData = requestData;
    }

    @Override
    protected String getLogTag() {
        return GetAllProfilesTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, requestData, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), GetAllProfilesTask.class.getSimpleName());
        }

        return getResponse(cursor);
    }

    @Override
    protected String getErrorMessage() {
        return "Unable to fetch all profiles!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.profiles/allProfiles", appQualifier);
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
