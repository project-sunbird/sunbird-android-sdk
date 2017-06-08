package org.ekstep.genieresolvers.language;

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
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, languageId, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR,
                    getErrorMessage(), GetTraversalRuleTask.class.getSimpleName());
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
        return "Couldn't get the traversal rules";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.languages/traversalrule", appQualifier);
        return Uri.parse(authority);
    }


}