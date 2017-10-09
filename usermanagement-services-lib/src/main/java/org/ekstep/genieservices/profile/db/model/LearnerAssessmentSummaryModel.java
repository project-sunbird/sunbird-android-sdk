package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
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
    private String hierarchyData;
    private List<LearnerAssessmentSummary> assessmentMap;

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
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent), h_data from " + LearnerAssessmentsEntry.TABLE_NAME + " where uid = '" + uid + "' group by content_id ";
    }

    private static String getContentProgressQuery(String contentId) {
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent), h_data from " + LearnerAssessmentsEntry.TABLE_NAME + " where content_id = '" + contentId + "' group by uid";
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                LearnerAssessmentSummary learnerAssessmentSummary = readCursorData(cursor);
                assessmentMap.add(learnerAssessmentSummary);
            } while (cursor.moveToNext());
        }
        return this;
    }

    private LearnerAssessmentSummary readCursorData(IResultSet cursor) {
        LearnerAssessmentSummary learnerAssessmentSummary = new LearnerAssessmentSummary();
        this.uid = cursor.getString(0);
        learnerAssessmentSummary.setUid(this.uid);

        this.contentId = cursor.getString(1);
        learnerAssessmentSummary.setContentId(this.contentId);

        this.noOfQuestions = cursor.getInt(2);
        learnerAssessmentSummary.setNoOfQuestions(this.noOfQuestions);

        this.correctAnswers = cursor.getInt(3);
        learnerAssessmentSummary.setCorrectAnswers(this.correctAnswers);

        this.totalTimespent = cursor.getDouble(4);
        learnerAssessmentSummary.setTotalTimespent(this.totalTimespent);

        this.hierarchyData = cursor.getString(5);
        learnerAssessmentSummary.setHierarchyData(this.hierarchyData);


        return learnerAssessmentSummary;
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

    public List<LearnerAssessmentSummary> getAssessmentMap() {
        if (assessmentMap == null) {
            assessmentMap = new ArrayList<>();
        }
        return assessmentMap;
    }

}
