package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private List<Map<String, Object>> reportsMap;
    private boolean forReports = false;

    private LearnerAssessmentSummaryModel(IDBSession dbSession) {
        assessmentMap = new ArrayList<>();
        this.dbSession = dbSession;
    }

    private LearnerAssessmentSummaryModel(IDBSession dbSession, boolean forReports) {
        reportsMap = new ArrayList<>();
        this.dbSession = dbSession;
        this.forReports = forReports;
    }

    public static LearnerAssessmentSummaryModel findChildProgressSummary(IDBSession idbSession, List<String> uids) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = new LearnerAssessmentSummaryModel(idbSession);
        idbSession.read(learnerAssessmentSummaryModel, getChildProgressQuery(uids));
        return learnerAssessmentSummaryModel;
    }

    public static LearnerAssessmentSummaryModel findContentProgressSummary(IDBSession idbSession, String contentId) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = new LearnerAssessmentSummaryModel(idbSession);
        idbSession.read(learnerAssessmentSummaryModel, getContentProgressQuery(contentId));
        return learnerAssessmentSummaryModel;
    }

    public static LearnerAssessmentSummaryModel findReportsSummary(IDBSession idbSession, List<String> uids, String contentId, boolean forReports) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = new LearnerAssessmentSummaryModel(idbSession, forReports);
        idbSession.read(learnerAssessmentSummaryModel, getUserReportsQuery(uids, contentId));
        return learnerAssessmentSummaryModel;
    }

    private static String getChildProgressQuery(List<String> uids) {
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent), h_data from " +
                LearnerAssessmentsEntry.TABLE_NAME + " where uid IN ('" + StringUtil.join("','", uids) +
                "') group by content_id ";
    }

    private static String getContentProgressQuery(String contentId) {
        return "select uid, content_id, count(qid), sum(correct), sum(time_spent), h_data from " +
                LearnerAssessmentsEntry.TABLE_NAME + " where content_id = '" + contentId + "' group by uid";
    }

    private static String getUserReportsQuery(List<String> uids, String contentId) {
        String query = String.format(Locale.US, "SELECT sum(%s),sum(%s),la.%s,la.%s,la.%s,p.%s" +
                        " FROM  %s la " +
                        "LEFT JOIN " +
                        "%s p ON la.%s = p.%s where la.%s IN('%s') AND la.%s = '%s' ORDER BY la.%s;",
                LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT,
                LearnerAssessmentsEntry.COLUMN_NAME_CORRECT,
                LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA,
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                ProfileEntry.COLUMN_NAME_HANDLE,
                LearnerAssessmentsEntry.TABLE_NAME,
                ProfileEntry.TABLE_NAME,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                ProfileEntry.COLUMN_NAME_UID,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                StringUtil.join(",", uids),
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                contentId,
                LearnerAssessmentsEntry.COLUMN_NAME_UID);

        return query;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (!this.forReports) {
                    LearnerAssessmentSummary learnerAssessmentSummary = readCursorData(cursor);
                    assessmentMap.add(learnerAssessmentSummary);
                } else {
                    Map<String, Object> report = readReportsCursorData(cursor);
                    reportsMap.add(report);
                }
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

    private Map<String, Object> readReportsCursorData(IResultSet cursor) {
        Map<String, Object> reportSummary = new HashMap<>();

        int totalTimeSpent = cursor.getInt(0);
        reportSummary.put("totalTimespent", totalTimeSpent);

        int score = cursor.getInt(1);
        reportSummary.put("score", score);

        String hData = cursor.getString(2);
        reportSummary.put("hData", hData);

        String contentId = cursor.getString(3);
        reportSummary.put("contentId", contentId);

        String uid = cursor.getString(4);
        reportSummary.put("uid", uid);

        String userName = cursor.getString(5);
        reportSummary.put("userName", userName);

        return reportSummary;
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

    public List<Map<String, Object>> getReportsMap() {
        if (reportsMap == null) {
            reportsMap = new ArrayList<>();
        }
        return reportsMap;
    }

}
