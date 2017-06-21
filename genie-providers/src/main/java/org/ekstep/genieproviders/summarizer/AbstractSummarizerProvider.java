package org.ekstep.genieproviders.summarizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.google.gson.Gson;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.content.AllContentsUriHandler;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.SummaryRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/6/17.
 * shriharsh
 */

public abstract class AbstractSummarizerProvider extends BaseContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = null;
        GenieResponse genieResponse;
        if (selectionArgs != null) {
            cursor = getMatrixCursor();

            String uid = selectionArgs[0];
            String contentId = selectionArgs[1];
            String hierarchyData = selectionArgs[2];
            SummaryRequest.Builder summaryRequestBuilder = new SummaryRequest.Builder();
            summaryRequestBuilder.contentId(contentId);
            summaryRequestBuilder.uid(uid);
            summaryRequestBuilder.hierarchyData(hierarchyData);
            genieResponse = getService().getSummarizerService().getLearnerAssessmentDetails(summaryRequestBuilder.build());

            if (genieResponse != null && genieResponse.getStatus()) {
                List<LearnerAssessmentDetails> learnerAssessmentDetailsList = (List<LearnerAssessmentDetails>) genieResponse.getResult();
                int totalQuestions = 0;
                int totalCorrect = 0;
                Map<String, Integer> resultMap = new HashMap<>();
                if (learnerAssessmentDetailsList != null && learnerAssessmentDetailsList.size() > 0) {
                    totalQuestions = learnerAssessmentDetailsList.size();
                    for (LearnerAssessmentDetails learnerAssessmentDetails : learnerAssessmentDetailsList) {
                        totalCorrect = totalCorrect + learnerAssessmentDetails.getCorrect();
                    }
                }

                resultMap.put("total_correct", totalCorrect);
                resultMap.put("total_questions", totalQuestions);

                GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse("Successful");
                successResponse.setResult(resultMap);

                cursor.addRow(new String[]{new Gson().toJson(successResponse)});

                return cursor;
            } else {
                getErrorResponse(cursor);
            }
        }

        return null;
    }

    protected Cursor getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not get learner assessments!", AllContentsUriHandler.class.getSimpleName());
        cursor.addRow(new String[]{new Gson().toJson(errorResponse)});
        return cursor;
    }

    private MatrixCursor getMatrixCursor() {
        return new MatrixCursor(new String[]{"values"});
    }

    @Override
    public String getType(Uri uri) {
        return String.format("vnd.android.cursor.item/%s.provider.summarizer", getPackageName());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String getCompletePath() {
        String SUMMARIZER_PATH = "summarizer";
        return String.format("%s.%s", getPackageName(), SUMMARIZER_PATH);
    }
}
