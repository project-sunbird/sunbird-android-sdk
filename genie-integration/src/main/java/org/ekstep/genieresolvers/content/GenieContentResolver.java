package org.ekstep.genieresolvers.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.HashMap;
import java.util.Map;

public class GenieContentResolver {
    private String contentId;
    private String appQualifier;
    private ContentResolver contentResolver;
    private Context context;

    public GenieContentResolver(Context context, String appQualifier, String contentId) {
        this.contentId = contentId;
        this.appQualifier = appQualifier;
        this.context = context;
    }

    public String execute() {
        Gson gson = new Gson();

        try {
            contentResolver = context.getContentResolver();

            if (contentResolver == null) {
                String logMessage = "Content Resolver for games not resolved";
                String errorMessage = "Not able to resolve content provider, " + getErrorMessage();
                GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, errorMessage, logMessage);
                return gson.toJson(errorResponse);
            }

            Cursor cursor = contentResolver.query(getUri(), null, null, new String[]{contentId}, "");

            if (cursor == null || cursor.getCount() == 0) {
                String logMessage = String.format("No response for content id:%s", contentId);
                GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR, getErrorMessage(), logMessage);
                return gson.toJson(errorResponse);
            }

            Map<String, Object> getContent = getPath(cursor);
            GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
            response.setResult(getContent);
            return gson.toJson(response);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Latest Genie is not installed";
            String logMessage = getErrorMessage() + ", because latest genie is not installed";
            GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.GENIE_SERVICE_NOT_INSTALLED, errorMessage, logMessage);
            return gson.toJson(errorResponse);
        }
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

    private String getErrorMessage() {
        return "content not found with the content-id:" + contentId;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/", appQualifier);
        return Uri.parse(authority);
    }

}
