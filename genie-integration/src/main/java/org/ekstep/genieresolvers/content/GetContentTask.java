package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
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
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), logMessage);
        }
        Map<String, Object> getContent = getPath(cursor);
        GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        response.setResult(getContent);
        return response;
    }

    private Map<String, Object> getPath(Cursor cursor) {
        HashMap<String, Object> content;
        cursor.moveToFirst();
        do {
            content = readContent(cursor);
        } while (cursor.moveToNext());
        cursor.close();
        return content;
    }

    private HashMap<String, Object> readContent(Cursor cursor) {
        HashMap<String, Object> content = new HashMap<>();
        content.put("identifier", cursor.getString(1));
        String serverData = cursor.getString(2);
        HashMap serverJson = GsonUtil.fromJson(serverData, HashMap.class);
        String localData = cursor.getString(3);
        boolean hasLocalData = localData != null && !localData.isEmpty();
        if (hasLocalData) {
            HashMap localJson = GsonUtil.fromJson(localData, HashMap.class);
            content.put("localData", localJson);
        }
        content.put("serverData", serverJson);
        content.put("mimeType", cursor.getString(4));
        content.put("path", cursor.getString(5));
        content.put("isAvailable", hasLocalData);
        return content;
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
