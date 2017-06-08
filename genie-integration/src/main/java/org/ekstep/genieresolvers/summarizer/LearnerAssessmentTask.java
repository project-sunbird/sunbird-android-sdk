package org.ekstep.genieresolvers.summarizer;

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

/**
 * Created on 6/6/17.
 * shriharsh
 */

class LearnerAssessmentTask extends BaseTask {
    private String uid;
    private String contentId;
    private String appQualifier;

    public LearnerAssessmentTask(Context context, String appQualifier, String uid, String contentId) {
        super(context);
        this.appQualifier = appQualifier;
        this.uid = uid;
        this.contentId = contentId;
    }

    @Override
    protected String getLogTag() {
        return LearnerAssessmentTask.class.getSimpleName();
    }

    @Override
    protected GenieResponse<Map> execute() {
        Cursor cursor = contentResolver.query(getUri(), null, null, new String[]{uid, contentId}, null);
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
        Type type = new TypeToken<GenieResponse<Map>>() {
        }.getType();
        GenieResponse<Map> response = GsonUtil.fromJson(result, type);
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
