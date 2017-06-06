package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentData;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 4/6/17.
 * shriharsh
 */

public class LearnerAssessmentsModel implements IReadable, IWritable {

    private Long id;
    private String uid;
    private String contentId;
    private String qid;
    private Double qindex;
    private Integer correct;
    private Double score;
    private Double timespent;
    private String res;
    private Long timestamp;
    private String qdesc;
    private String qtitle;
    private IDBSession dbSession;
    private List<LearnerAssessmentData> mAssessmentList;


    private LearnerAssessmentsModel(String uid, String contentId) {
        this.uid = uid;
        this.contentId = contentId;
    }

    private LearnerAssessmentsModel(LearnerAssessmentData learnerAssessmentData) {
        this.uid = learnerAssessmentData.getUid();
        this.contentId = learnerAssessmentData.getContentId();
        this.qid = learnerAssessmentData.getQid();
        this.qindex = learnerAssessmentData.getQindex();
        String pass = learnerAssessmentData.getPass();
        this.correct = ("Yes".equalsIgnoreCase(pass) ? 1 : 0);
        this.score = learnerAssessmentData.getScore();
        this.timespent = learnerAssessmentData.getTimespent();
        if ("2.0".equalsIgnoreCase(learnerAssessmentData.getVer())) {
            this.timestamp = learnerAssessmentData.getTimestamp();
            this.res = learnerAssessmentData.getRes();
        } else {
            this.timestamp = DateUtil.dateToEpoch(Long.toString(learnerAssessmentData.getTimestamp()));
            this.res = learnerAssessmentData.getRes();
        }
        this.qdesc = learnerAssessmentData.getQdesc();
        this.qtitle = learnerAssessmentData.getQtitle();
    }

    private LearnerAssessmentsModel(IDBSession dbSession, LearnerAssessmentData learnerAssessmentData) {
        this(learnerAssessmentData);
        this.dbSession = dbSession;
    }

    public static LearnerAssessmentsModel build(IDBSession dbSession, LearnerAssessmentData learnerAssessmentData) {
        return new LearnerAssessmentsModel(dbSession, learnerAssessmentData);
    }

    public static LearnerAssessmentsModel findAllAssessments(IDBSession dbSession) {
        LearnerAssessmentsModel learnerAssessmentsModel = new LearnerAssessmentsModel(null);
        dbSession.read(learnerAssessmentsModel);

        return learnerAssessmentsModel;
    }

    public static LearnerAssessmentsModel findAssessmentById(IDBSession dbSession, String uid, String contentId) {
        LearnerAssessmentsModel learnerAssessmentsModel = new LearnerAssessmentsModel(uid, contentId);
        dbSession.read(learnerAssessmentsModel);

        return learnerAssessmentsModel;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            mAssessmentList = new ArrayList<>();

            do {
                LearnerAssessmentData learnerAssessmentData = getLearnerAssessmentData(resultSet);
                mAssessmentList.add(learnerAssessmentData);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    private LearnerAssessmentData getLearnerAssessmentData(IResultSet cursor) {
        LearnerAssessmentData learnerAssessmentData = new LearnerAssessmentData();

        learnerAssessmentData.setUid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_UID)));

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID) != -1) {
            learnerAssessmentData.setContentId(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID) != -1) {
            learnerAssessmentData.setQid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QINDEX) != -1) {
            learnerAssessmentData.setQindex(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT) != -1) {
            learnerAssessmentData.setCorrect(cursor.getInt(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE) != -1) {
            learnerAssessmentData.setScore(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT) != -1) {
            learnerAssessmentData.setTimespent(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES) != -1) {
            learnerAssessmentData.setRes(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP) != -1) {
            learnerAssessmentData.setTimestamp(cursor.getLong(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QDESC) != -1) {
            learnerAssessmentData.setQdesc(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QDESC)));
        }

        if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QTITLE) != -1) {
            learnerAssessmentData.setQtitle(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QTITLE)));
        }


        return learnerAssessmentData;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_UID, this.uid);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QID, this.qid);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QINDEX, this.qindex);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT, this.correct);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_SCORE, this.score);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT, this.timespent);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_RES, this.res);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP, this.timestamp);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QDESC, this.qdesc);
        values.put(LearnerAssessmentsEntry.COLUMN_NAME_QTITLE, this.qtitle);
        return values;
    }

    @Override
    public void updateId(long id) {

    }

    @Override
    public String getTableName() {
        return LearnerAssessmentsEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "order by " + LearnerAssessmentsEntry.COLUMN_NAME_QINDEX;
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ? and %s = ? ", LearnerAssessmentsEntry.COLUMN_NAME_UID, LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID);

    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{uid, contentId};
    }

    @Override
    public String limitBy() {
        return "";
    }

    public void save() {
        dbSession.create(this);
    }

    public List<LearnerAssessmentData> getAllAssesments() {
        if (mAssessmentList == null) {
            mAssessmentList = new ArrayList<>();
        }
        return mAssessmentList;
    }
}
