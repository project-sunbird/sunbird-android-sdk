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
import java.util.List;
import java.util.Map;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetRelatedContentTask extends BaseTask {

    private String userId;
    private String appQualifier;
    private List<String> contentIdentifiers;

    public GetRelatedContentTask(Context context, String appQualifier, List<String> contentIdentifiers, String userId) {
        super(context);
        this.appQualifier = appQualifier;
        this.contentIdentifiers = contentIdentifiers;
        this.userId = userId;
    }

    @Override
    protected String getLogTag() {
        return GetRelatedContentTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        String requestData = GsonUtil.toJson(contentIdentifiers);
        Cursor cursor = contentResolver.query(getUri(), null, requestData, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR,
                    getErrorMessage(), GetRelatedContentTask.class.getSimpleName());
        }
        return getResponse(cursor);
    }

    @Override
    protected String getErrorMessage() {
        return "Related content is not found with the content-ids";
    }

    private GenieResponse<Map> getResponse(Cursor cursor) {
        GenieResponse<Map> mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            mapData = readCursor(cursor);
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse readCursor(Cursor cursor) {
        String resultData = cursor.getString(0);
        Type type = new TypeToken<GenieResponse<Map>>() {
        }.getType();
        GenieResponse response = GsonUtil.fromJson(resultData, type);
        return response;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/relatedContent", appQualifier);
        return Uri.parse(authority);
    }

}
