package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummaryResponse;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 5/6/17.
 * shriharsh
 */

public class LearnerAssessmentSummaryModel implements IReadable {

    private IDBSession dbSession;
    private String uid;
    private String contentId;
    private Integer noOfQuestions;
    private Integer correctAnswers;
    private Double totalTimespent;
    private List<LearnerAssessmentSummaryResponse> assessmentMap;

    private LearnerAssessmentSummaryModel(IDBSession dbSession) {
        assessmentMap = new ArrayList<>();
        this.dbSession = dbSession;

    }

    public static LearnerAssessmentSummaryModel findChildProgressSummary(IDBSession idbSession, String uid) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = new LearnerAssessmentSummaryModel(idbSession);
        idbSession.read(learnerAssessmentSummaryModel, getChildProgressQuery(uid));
        return learnerAssessmentSummaryModel;
    }

    public static LearnerAssessmentSummaryModel findContentProgressSummary(IDBSession idbSession, String contentId) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = new LearnerAssessmentSummaryModel(idbSession);
        idbSession.read(learnerAssessmentSummaryModel, getContentProgressQuery(contentId));
        return learnerAssessmentSummaryModel;
    }

    private static String getChildProgressQuery(String uid) {
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent) from " + LearnerAssessmentsEntry.TABLE_NAME + " where uid = '" + uid + "' group by content_id ";
    }

    private static String getContentProgressQuery(String contentId) {
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent) from " + LearnerAssessmentsEntry.TABLE_NAME + " where content_id = '" + contentId + "' group by uid";
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                LearnerAssessmentSummaryResponse learnerAssessmentSummaryResponse = readCursorData(cursor);
                assessmentMap.add(learnerAssessmentSummaryResponse);
            } while (cursor.moveToNext());
        }
        return this;
    }

    private LearnerAssessmentSummaryResponse readCursorData(IResultSet cursor) {
        LearnerAssessmentSummaryResponse learnerAssessmentSummaryResponse = new LearnerAssessmentSummaryResponse();
        this.uid = cursor.getString(0);
        learnerAssessmentSummaryResponse.setUid(this.uid);

        this.contentId = cursor.getString(1);
        learnerAssessmentSummaryResponse.setContent_id(this.contentId);

        this.noOfQuestions = cursor.getInt(2);
        learnerAssessmentSummaryResponse.setNoOfQuestions(this.noOfQuestions);

        this.correctAnswers = cursor.getInt(3);
        learnerAssessmentSummaryResponse.setCorrectAnswers(this.correctAnswers);

        this.totalTimespent = cursor.getDouble(4);
        learnerAssessmentSummaryResponse.setTotalTimespent(this.totalTimespent);

        return learnerAssessmentSummaryResponse;
    }

    @Override
    public String getTableName() {
        return LearnerAssessmentsEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<LearnerAssessmentSummaryResponse> getAssessmentMap() {
        if (assessmentMap == null) {
            assessmentMap = new ArrayList<>();
        }
        return assessmentMap;
    }

}
