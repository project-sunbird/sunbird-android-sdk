package org.ekstep.genieresolvers.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class GetRelatedContentTask extends BaseTask {

    private String userId;
    private String appQualifier;
    private List<HashMap<String, Object>> contentIdentifiers;

    public GetRelatedContentTask(Context context, String appQualifier, List<HashMap<String, Object>> contentIdentifiers, String userId) {
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
    protected GenieResponse execute() {
        RelatedContentRequest request = new RelatedContentRequest(userId, contentIdentifiers);
        String requestData = GsonUtil.toJson(request);
        Cursor cursor = contentResolver.query(getUri(), null, requestData, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            String logMessage = "Couldn't get the related content data.";
            return getErrorResponse(Constants.PROCESSING_ERROR,
                    getErrorMessage(), logMessage);
        }
        GenieResponse response = getResponse(cursor);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Related content is not found with the content-id";
    }

    private GenieResponse getResponse(Cursor cursor) {
        GenieResponse mapData = null;
        if (cursor != null && cursor.moveToFirst()) {
            mapData = readCursor(cursor);
            cursor.close();
        }
        return mapData;
    }

    private GenieResponse readCursor(Cursor cursor) {
        String resultData = cursor.getString(0);
        GenieResponse response = GsonUtil.fromJson(resultData, GenieResponse.class);
        return response;
    }

    private Uri getUri() {
        String authority = String.format("content://%s.content/relatedContent", appQualifier);
        return Uri.parse(authority);
    }

}
