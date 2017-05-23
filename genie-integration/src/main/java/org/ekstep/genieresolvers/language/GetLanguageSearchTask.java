package org.ekstep.genieresolvers.language;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetLanguageSearchTask extends BaseTask {
    private String searchRequest;
    private String appQualifier;

    public GetLanguageSearchTask(Context context, String appQualifier, String searchRequest) {
        super(context);
        this.appQualifier = appQualifier;
        this.searchRequest = searchRequest;
    }

    @Override
    protected String getLogTag() {
        return null;
    }

    @Override
    protected String execute() {
        Gson gson = new Gson();
        Cursor cursor = contentResolver.query(getUri(), null, searchRequest, null, "");
        if (cursor == null) {
            String logMessage = "Couldn't get the language search data";
            GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ProviderResolver.PROCESSING_ERROR,
                    getErrorMessage(), logMessage);
            return gson.toJson(errorResponse);
        }
        GenieResponse genieResponse = getResponse(cursor);
        return gson.toJson(genieResponse);
    }

    private GenieResponse getResponse(Cursor cursor) {
        GenieResponse mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mapData = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse readCursor(Cursor cursor) {
        Gson gson = new Gson();
        String serverData = cursor.getString(0);
        GenieResponse response = gson.fromJson(serverData, GenieResponse.class);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return null;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.languages/langsearch", appQualifier);
        return Uri.parse(authority);
    }
}
