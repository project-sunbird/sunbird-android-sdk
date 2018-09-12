package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerSummaryEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public class LearnerAssessmentDetailsModel implements IReadable, IWritable, IUpdatable, ICleanable {

    //Need to passed when finding for the model
    public static final int FOR_SUMMARIZER = 1;
    public static final int FOR_QUESTIONS_REPORT = 2;
    public static final int FOR_QUESTION_DETAILS = 3;
    public static final int FOR_ACCURACY_REPORT = 4;

    private Long id = -1L;
    private String uid;
    private String contentId;
    private String qid;
    private Double qindex;
    private Integer correct;
    private Double score;
    private Double maxScore;
    private Double timespent;
    private String res;
    private Long timestamp;
    private String qdesc;
    private String qtitle;
    private String hierarchyData;
    private IDBSession dbSession;
    private List<LearnerAssessmentDetails> mAssessmentList;
    private String filter;
    private List<Map<String, Object>> reportsMapList;
    private Map<Double, Integer> accuracyMap;
    private int forReports = 1;


    private LearnerAssessmentDetailsModel(IDBSession dbSession, String filter) {
        this.dbSession = dbSession;
        this.filter = filter;
    }

    private LearnerAssessmentDetailsModel(IDBSession dbSession, LearnerAssessmentDetails learnerAssessmentDetails) {
        this.dbSession = dbSession;

        this.uid = learnerAssessmentDetails.getUid();
        this.contentId = learnerAssessmentDetails.getContentId();
        this.qid = learnerAssessmentDetails.getQid();
        this.qindex = learnerAssessmentDetails.getQindex();
        this.correct = learnerAssessmentDetails.getCorrect();
        this.score = learnerAssessmentDetails.getScore();
        this.timespent = learnerAssessmentDetails.getTimespent();
        this.timestamp = learnerAssessmentDetails.getTimestamp();
        this.res = learnerAssessmentDetails.getRes();
        this.qdesc = learnerAssessmentDetails.getQdesc();
        this.qtitle = learnerAssessmentDetails.getQtitle();
        this.maxScore = learnerAssessmentDetails.getMaxScore();
        this.hierarchyData = learnerAssessmentDetails.getHierarchyData();
    }

    public LearnerAssessmentDetailsModel(IDBSession dbSession, int forReports) {
        if (forReports == FOR_ACCURACY_REPORT) {
            accuracyMap = new HashMap<>();
        } else {
            reportsMapList = new ArrayList<>();
        }

        this.dbSession = dbSession;
        this.forReports = forReports;
    }

    public static LearnerAssessmentDetailsModel build(IDBSession dbSession, LearnerAssessmentDetails learnerAssessmentDetails) {
        return new LearnerAssessmentDetailsModel(dbSession, learnerAssessmentDetails);
    }

    public static LearnerAssessmentDetailsModel find(IDBSession dbSession, String filter) {
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = new LearnerAssessmentDetailsModel(dbSession, filter);
        dbSession.read(learnerAssessmentDetailsModel);

        if (learnerAssessmentDetailsModel.mAssessmentList == null) {
            return null;
        } else {
            return learnerAssessmentDetailsModel;
        }
    }

    public static LearnerAssessmentDetailsModel findDetailReport(IDBSession dbSession, List<String> quotedUIds, String contentId, int forDetailReport) {
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = new LearnerAssessmentDetailsModel(dbSession, forDetailReport);
        dbSession.read(learnerAssessmentDetailsModel, getDetailReportsQuery(quotedUIds, contentId));

        if (learnerAssessmentDetailsModel.mAssessmentList == null) {
            return null;
        } else {
            return learnerAssessmentDetailsModel;
        }
    }

    public static LearnerAssessmentDetailsModel findForQuestionAccuracy(IDBSession dbSession, String contentId, List<String> uids, int forReports) {
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = new LearnerAssessmentDetailsModel(dbSession, forReports);
        dbSession.read(learnerAssessmentDetailsModel, getAccuracyReportsQuery(uids, contentId));
        return learnerAssessmentDetailsModel;
    }

    public static LearnerAssessmentDetailsModel findQuestionsReportSummary(IDBSession dbSession, String contentId, List<String> uids, int forReports) {
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = new LearnerAssessmentDetailsModel(dbSession, forReports);
        dbSession.read(learnerAssessmentDetailsModel, getQuestionReportsQuery(uids, contentId));
        return learnerAssessmentDetailsModel;
    }

    public static LearnerAssessmentDetailsModel findQuestionDetails(IDBSession dbSession, List<String> uids, String contentId, String qId, int forReports) {
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = new LearnerAssessmentDetailsModel(dbSession, forReports);
        dbSession.read(learnerAssessmentDetailsModel, getQuestionDetailsQuery(uids, contentId, qId));
        return learnerAssessmentDetailsModel;
    }

    private static String getDetailReportsQuery(List<String> quotedUIds, String contentId) {

        String query = String.format(Locale.US, "SELECT *, lcs.%s " +
                        " FROM  %s la " +
                        "LEFT JOIN %s lcs ON (la.%s = lcs.%s AND la.%s = lcs.%s) " +
                        "where la.%s IN(%s) AND la.%s = '%s';",
                LearnerSummaryEntry.COLUMN_NAME_TOTAL_TS,
                LearnerAssessmentsEntry.TABLE_NAME,
                LearnerSummaryEntry.TABLE_NAME,
                LearnerSummaryEntry.COLUMN_NAME_UID,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                LearnerSummaryEntry.COLUMN_NAME_CONTENT_ID,
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                StringUtil.join(",", quotedUIds),
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                contentId);

        return query;
    }

    private static String getAccuracyReportsQuery(List<String> uids, String contentId) {

        String query = String.format(Locale.US, "SELECT %s, count(*) as users_count " +
                        "FROM  %s " +
                        "WHERE %s IN(%s) AND %s = '%s' AND %s > 0 " +
                        "group by %s;",
                LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX,
                LearnerAssessmentsEntry.TABLE_NAME,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                StringUtil.join(",", uids),
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                contentId,
                LearnerAssessmentsEntry.COLUMN_NAME_CORRECT,
                LearnerAssessmentsEntry.COLUMN_NAME_QID);

        return query;
    }

    private static String getQuestionDetailsQuery(List<String> uids, String contentId, String qId) {

        return String.format(Locale.US, "SELECT %s, %s as time , %s as result, %s as max_score " +
                        "FROM  %s " +
                        "WHERE %s IN(%s) AND %s = '%s' AND %s = '%s';",
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT,
                LearnerAssessmentsEntry.COLUMN_NAME_CORRECT,
                LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE,
                LearnerAssessmentsEntry.TABLE_NAME,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                StringUtil.join(",", uids),
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                contentId,
                LearnerAssessmentsEntry.COLUMN_NAME_QID,
                qId);
    }

    private static String getQuestionReportsQuery(List<String> uids, String contentId) {
        String query = String.format(Locale.US, "SELECT *, sum(%s) as marks , count(%s) as count,sum(%s) maxscore " +
                        "FROM  %s " +
                        "WHERE %s IN(%s) AND %s = '%s'  group by %s;",
                LearnerAssessmentsEntry.COLUMN_NAME_SCORE,
                LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX,
                LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE,
                LearnerAssessmentsEntry.TABLE_NAME,
                LearnerAssessmentsEntry.COLUMN_NAME_UID,
                StringUtil.join(",", uids),
                LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID,
                contentId,
                LearnerAssessmentsEntry.COLUMN_NAME_QID);

        return query;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            mAssessmentList = new ArrayList<>();

            do {

                switch (forReports) {
                    case FOR_SUMMARIZER:
                        LearnerAssessmentDetails learnerAssessmentDetails = getLearnerAssessmentData(resultSet);
                        mAssessmentList.add(learnerAssessmentDetails);
                        break;
                    case FOR_QUESTIONS_REPORT:
                        Map<String, Object> questionsReport = readQuestionsReportsCursorData(resultSet);
                        reportsMapList.add(questionsReport);
                        break;
                    case FOR_QUESTION_DETAILS:
                        Map<String, Object> questionDetailReport = readQuestionDetailReportsCursorData(resultSet);
                        reportsMapList.add(questionDetailReport);
                        break;
                    case FOR_ACCURACY_REPORT:
                        readAccuracyReportCursorData(resultSet);
                        break;

                }
            } while (resultSet.moveToNext());
        }

        return this;
    }

    private void readAccuracyReportCursorData(IResultSet cursor) {
        double qIndex = cursor.getDouble(0);
        int correct_count = cursor.getInt(1);

        accuracyMap.put(qIndex, correct_count);
    }

    private Map<String, Object> readQuestionDetailReportsCursorData(IResultSet cursor) {
        Map<String, Object> reportSummary = new HashMap<>();

        String uid = cursor.getString(0);
        reportSummary.put("uid", uid);

        int time = cursor.getInt(1);
        reportSummary.put("time", time);

        int result = cursor.getInt(2);
        reportSummary.put("result", result);

        int maxScore = cursor.getInt(3);
        reportSummary.put("maxScore", maxScore);

        return reportSummary;
    }

    private Map<String, Object> readQuestionsReportsCursorData(IResultSet cursor) {
        Map<String, Object> reportSummary = new HashMap<>();

        reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_UID, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_UID)));

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_QID, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX, cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT, cursor.getInt(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_SCORE, cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT, cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_RES, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP, cursor.getLong(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE) != -1) {
            reportSummary.put(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE, cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE)));
        }

        //14 marks
        int marks = cursor.getInt(14);
        reportSummary.put("marks", marks);

        //15 count
        int count = cursor.getInt(15);
        reportSummary.put("occurenceCount", count);

        int sum_max_score = cursor.getInt(16);
        reportSummary.put("sum_max_score", count);

        return reportSummary;
    }

    private LearnerAssessmentDetails getLearnerAssessmentData(IResultSet cursor) {
        LearnerAssessmentDetails learnerAssessmentDetails = new LearnerAssessmentDetails();

        learnerAssessmentDetails.setUid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_UID)));

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID) != -1) {
            learnerAssessmentDetails.setContentId(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID) != -1) {
            learnerAssessmentDetails.setQid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX) != -1) {
            learnerAssessmentDetails.setQindex(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT) != -1) {
            learnerAssessmentDetails.setCorrect(cursor.getInt(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE) != -1) {
            learnerAssessmentDetails.setScore(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT) != -1) {
            learnerAssessmentDetails.setTimespent(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES) != -1) {
            learnerAssessmentDetails.setRes(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP) != -1) {
            learnerAssessmentDetails.setTimestamp(cursor.getLong(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC) != -1) {
            learnerAssessmentDetails.setQdesc(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE) != -1) {
            learnerAssessmentDetails.setQtitle(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA) != -1) {
            learnerAssessmentDetails.setHierarchyData(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE) != -1) {
            learnerAssessmentDetails.setMaxScore(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE)));
        }

        if (cursor.getColumnIndex(LearnerSummaryEntry.COLUMN_NAME_TOTAL_TS) != -1) {
            learnerAssessmentDetails.setTotal_ts(cursor.getDouble(cursor.getColumnIndex(LearnerSummaryEntry.COLUMN_NAME_TOTAL_TS)));
        }

        return learnerAssessmentDetails;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_UID, this.uid);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QID, this.qid);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX, this.qindex);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT, this.correct);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_SCORE, this.score);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE, this.maxScore);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT, this.timespent);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_RES, this.res);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP, this.timestamp);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC, this.qdesc);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE, this.qtitle);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, this.hierarchyData);
        return values;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues values = new ContentValues();
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QID, this.qid);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX, this.qindex);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT, this.correct);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_SCORE, this.score);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE, this.maxScore);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT, this.timespent);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_RES, this.res);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP, this.timestamp);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC, this.qdesc);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE, this.qtitle);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, this.hierarchyData);
        return values;
    }

    @Override
    public String getTableName() {
        return LearnerAssessmentsEntry.TABLE_NAME;
    }

    public Void delete() {
        dbSession.clean(this);
        return null;
    }

    @Override
    public void clean() {
        this.id = -1L;
        this.uid = null;
        this.contentId = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, " where %s = '%s' AND %s = '%s'",
                LearnerAssessmentsEntry.COLUMN_NAME_UID, this.uid, LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
    }

    @Override
    public String updateBy() {
        String isQid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_QID, qid);
        String isUid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_UID, uid);
        String isContentId = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, contentId);
        String isHData = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, hierarchyData == null ? "" : hierarchyData);

        return String.format(Locale.US, "%s AND %s AND %s AND %s", isUid, isContentId, isHData, isQid);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "order by " + LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX;
    }

    @Override
    public String filterForRead() {
        return filter;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public void save() {
        dbSession.create(this);
    }

    public void update() {
        dbSession.update(this);
    }

    public List<LearnerAssessmentDetails> getAllAssessments() {
        return mAssessmentList;
    }

    public List<Map<String, Object>> getReportsMapList() {
        if (reportsMapList == null) {
            reportsMapList = new ArrayList<>();
        }
        return reportsMapList;
    }

    public Map<Double, Integer> getAccuracyReportMap() {
        if (accuracyMap == null) {
            accuracyMap = new HashMap<>();
        }
        return accuracyMap;
    }
}
