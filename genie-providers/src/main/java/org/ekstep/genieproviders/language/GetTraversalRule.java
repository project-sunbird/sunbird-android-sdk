package org.ekstep.genieproviders.language;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class GetTraversalRule {
    private static final String TRAVERSAL_URI = "content://org.ekstep.genieservices.languages/traversalrule";
    private String languageId;
    private AppContext appContext;
    private ContentResolver contentResolver;

    public GetTraversalRule(String languageId, AppContext appContext) {
        this.languageId = languageId;
        this.appContext = appContext;
    }

    private String execute() {
        Gson gson = new Gson();

        contentResolver = ((Context) appContext.getContext()).getContentResolver();

        if (contentResolver == null) {
            String logMessage = "Content Resolver for games not resolved";
            String errorMessage = "Not able to resolve content provider, " + getErrorMessage();
            GenieResponse<String> errorResponse = GenieResponseBuilder.getErrorResponse(ProviderConstants.PROCESSING_ERROR, errorMessage, logMessage);
            return gson.toJson(errorResponse);
        }

        Cursor cursor = contentResolver.query(getUri(), null, languageId, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            String logMessage = "Couldn't get the traversal rules";
            GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(ProviderConstants.PROCESSING_ERROR,
                    getErrorMessage(), logMessage);
            return gson.toJson(errorResponse);
        }
        GenieResponse<String> genieResponse = getResponse(cursor);
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

    private String getErrorMessage() {
        return "Couldn't get the traversal rules";
    }

    private Uri getUri() {
        return Uri.parse(TRAVERSAL_URI);
    }

}