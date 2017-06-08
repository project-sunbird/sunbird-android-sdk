package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.Map;

public class GetContentTask extends BaseTask {
    private static final String TAG = GetContentsTask.class.getSimpleName();
    private String contentId;
    private String appQualifier;

    public GetContentTask(Context context, String appQualifier, String contentId) {
        super(context);
        this.contentId = contentId;
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetContentTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, new String[]{contentId}, "");
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), TAG);
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
        Type type = new TypeToken<GenieResponse<Map>>() {
        }.getType();
        GenieResponse<Map> response = GsonUtil.fromJson(serverData, type);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "content not found with the content-id:" + contentId;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/", appQualifier);
        return Uri.parse(authority);
    }

}
