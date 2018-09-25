package org.ekstep.genieproviders.summarizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import org.ekstep.genieproviders.BaseContentProvider;
import org.ekstep.genieproviders.util.Constants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.ekstep.genieservices.commons.utils.GsonUtil;

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
        if (selection != null) {
            cursor = getMatrixCursor();

            Map data = GsonUtil.getGson().fromJson(selection, Map.class);

            String currentContentIdentifier = data.get("currentContentIdentifier").toString();
            String userId = data.get("userId").toString();
            SummaryRequest.Builder summaryRequestBuilder = new SummaryRequest.Builder();
            summaryRequestBuilder.contentId(currentContentIdentifier);
            summaryRequestBuilder.uid(userId);

            Map hierarchyData = (Map) data.get("hierarchyData");
            String hierarchyDataString = null;
            if (hierarchyData != null) {
                hierarchyDataString = hierarchyData.get("id") != null ? hierarchyData.get("id").toString() : null;
            }
            summaryRequestBuilder.hierarchyData(hierarchyDataString);
            genieResponse = getService().getSummarizerService().getLearnerAssessmentDetails(summaryRequestBuilder.build());

            if (genieResponse != null && genieResponse.getStatus()) {
                List<LearnerAssessmentDetails> learnerAssessmentDetailsList = (List<LearnerAssessmentDetails>) genieResponse.getResult();
                double totalMaxScore = 0;
                double totalScore = 0;
                Map<String, Double> resultMap = new HashMap<>();
                if (learnerAssessmentDetailsList != null && learnerAssessmentDetailsList.size() > 0) {
                    for (LearnerAssessmentDetails learnerAssessmentDetails : learnerAssessmentDetailsList) {

                        //Changed this because we decided to send totalMaxScore and totalScore, otherwise sending the totalCorect would not work correct
                        //in case of partial scoring
                        totalMaxScore = totalMaxScore + learnerAssessmentDetails.getMaxScore();
                        totalScore = totalScore + learnerAssessmentDetails.getScore();
                    }
                }

                resultMap.put("total_correct", totalScore);
                resultMap.put("total_questions", totalMaxScore);

                GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse("Successful");
                successResponse.setResult(resultMap);

                cursor.addRow(new String[]{GsonUtil.toJson(successResponse)});

                return cursor;
            }
        }

        return getErrorResponse(cursor);
    }

    protected Cursor getErrorResponse(MatrixCursor cursor) {
        GenieResponse errorResponse = GenieResponseBuilder.getErrorResponse(Constants.PROCESSING_ERROR, "Could not get learner assessments!", AbstractSummarizerProvider.class.getSimpleName());
        cursor.addRow(new String[]{GsonUtil.toJson(errorResponse)});
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

}
