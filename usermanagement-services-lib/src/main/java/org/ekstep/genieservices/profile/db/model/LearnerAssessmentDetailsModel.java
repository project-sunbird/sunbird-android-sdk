package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public class LearnerAssessmentDetailsModel implements IReadable, IWritable, IUpdatable {

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

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            mAssessmentList = new ArrayList<>();

            do {
                LearnerAssessmentDetails learnerAssessmentDetails = getLearnerAssessmentData(resultSet);
                mAssessmentList.add(learnerAssessmentDetails);
            } while (resultSet.moveToNext());
        }

        return this;
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

}
