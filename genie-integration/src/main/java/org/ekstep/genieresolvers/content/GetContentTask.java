package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

public class GetContentTask extends BaseTask {
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
    protected GenieResponse execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, new String[]{contentId}, "");
        if (cursor == null || cursor.getCount() == 0) {
            String logMessage = String.format("No response for content id:%s", contentId);
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), logMessage);
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
        return "content not found with the content-id:" + contentId;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/", appQualifier);
        return Uri.parse(authority);
    }

}
