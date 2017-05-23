package org.ekstep.genieresolvers.language;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class GetTraversalRuleTask extends BaseTask {
    private String languageId;
    private String appQualifier;

    public GetTraversalRuleTask(Context context, String appQualifier, String languageId) {
        super(context);
        this.languageId = languageId;
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetTraversalRuleTask.class.getSimpleName();
    }

    @Override
    protected String execute() {
        Gson gson = new Gson();
        Cursor cursor = contentResolver.query(getUri(), null, languageId, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            String logMessage = "Couldn't get the traversal rules";
            GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR,
                    getErrorMessage(), logMessage);
            return gson.toJson(errorResponse);
        }
        GenieResponse genieResponse = getResponse(cursor);
        return gson.toJson(genieResponse);
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
        return "Couldn't get the traversal rules";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.languages/traversalrule", appQualifier);
        return Uri.parse(authority);
    }


}