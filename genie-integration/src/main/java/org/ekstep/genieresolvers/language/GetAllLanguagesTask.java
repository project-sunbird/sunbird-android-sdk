package org.ekstep.genieresolvers.language;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Language;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetAllLanguagesTask extends BaseTask {
    private String appQualifier;

    public GetAllLanguagesTask(Context context, String appQualifier) {
        super(context);
        this.appQualifier = appQualifier;
    }

    @Override
    protected String getLogTag() {
        return GetAllLanguagesTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse execute() {
        try {
            Cursor cursor = contentResolver.query(getUri(), null, "", null, "");

            List<Language> languages = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Language language = new Language(cursor.getString(1), cursor.getString(2));
                    languages.add(language);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return getResponse(languages);
        } catch (SQLiteException ex) {
            String errorMessage = ex.getMessage();
            return getErrorResponse("DB_ERROR", errorMessage);
        }
    }

    @Override
    protected String getErrorMessage() {
        return null;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.languages/all", appQualifier);
        return Uri.parse(authority);
    }

    private GenieResponse<List<Map<String, Object>>> getResponse(List<Language> languages) {
        Gson gson = new Gson();
        GenieResponse<List<Map<String, Object>>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.ProviderResolver.SUCCESSFUL);
        String json = gson.toJson(languages);
        Type type = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> results = gson.fromJson(json, type);
        response.setResult(results);
        return response;
    }

    private GenieResponse<List<Map<String, Object>>> getErrorResponse(String error, String errorMessage) {
        return GenieResponseBuilder.getErrorResponse(error, errorMessage, getLogTag());
    }
}
