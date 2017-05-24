package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetContentsTask extends BaseTask {

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
            String logMessage = "Couldn't get the content list";
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), logMessage);
        }
        List<Map<String, Object>> contentPath = getPath(cursor);
        GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        response.setResult(contentPath);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Couldn't get the content list";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/list", appQualifier);
        return Uri.parse(authority);
    }

    private List<Map<String, Object>> getPath(Cursor cursor) {
        List<Map<String, Object>> contents = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                contents.add(readContent(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contents;
    }

    private HashMap<String, Object> readContent(Cursor cursor) {
        Gson gson = new Gson();
        HashMap<String, Object> content = new HashMap<>();
        content.put("identifier", cursor.getString(1));
        String serverData = cursor.getString(2);
        HashMap serverJson = gson.fromJson(serverData, HashMap.class);
        String localData = cursor.getString(3);
        boolean hasLocalData = localData != null && !localData.isEmpty();
        if (hasLocalData) {
            HashMap localJson = gson.fromJson(localData, HashMap.class);
            content.put("localData", localJson);
        }
        content.put("serverData", serverJson);
        content.put("mimeType", cursor.getString(4));
        content.put("path", cursor.getString(5));
        content.put("isAvailable", hasLocalData);
        return content;
    }

}
