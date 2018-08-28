package org.ekstep.genieresolvers.summarizer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/6/17.
 * shriharsh
 */

class LearnerAssessmentTask extends BaseTask {
    private String userId;
    private String currentContentIdentifier;
    private String appQualifier;
    private Map hierarchyData;

    public LearnerAssessmentTask(Context context, String appQualifier, String userId, String currentContentIdentifier, Map hierarchyData) {
        super(context);
        this.appQualifier = appQualifier;
        this.userId = userId;
        this.currentContentIdentifier = currentContentIdentifier;
        this.hierarchyData = hierarchyData;
    }

    @Override
    protected String getLogTag() {
        return LearnerAssessmentTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Map data = new HashMap();
        data.put("hierarchyData", hierarchyData);
        data.put("userId", userId);
        data.put("currentContentIdentifier", currentContentIdentifier);

        String requestData = GsonUtil.toJson(data);
        Cursor cursor = contentResolver.query(getUri(), null, requestData, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return getErrorResponse(Constants.PROCESSING_ERROR, getErrorMessage(), LearnerAssessmentTask.class.getSimpleName());
        }

        return getResponse(cursor);
    }

    private GenieResponse<Map> getResponse(Cursor cursor) {
        GenieResponse<Map> response = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                response = readCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return response;
    }

    private GenieResponse<Map> readCursor(Cursor cursor) {
        String result = cursor.getString(0);
        GenieResponse<Map> response = GsonUtil.fromJson(result, GenieResponse.class);
        return response;
    }

    @Override
    protected String getErrorMessage() {
        return "Could not find assessment summary!";
    }

    private Uri getUri() {
        String authority = String.format("content://%s.summarizer/learnerAssessment", appQualifier);
        return Uri.parse(authority);
    }
}
