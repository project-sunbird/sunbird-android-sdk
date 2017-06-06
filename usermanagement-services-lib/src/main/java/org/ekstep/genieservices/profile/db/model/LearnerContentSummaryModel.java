package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerSummaryData;
import org.ekstep.genieservices.commons.db.contract.LearnerContentSummaryEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Locale;

/**
 * Created on 5/6/17.
 * shriharsh
 */

public class LearnerContentSummaryModel implements IReadable, IWritable {
    private Long id;
    private String uid;
    private String contentId;
    private Double avgts;
    private Integer sessions;
    private Double totalts;
    private Long lastUpdated;
    private Double timespent;
    private Long timestamp;
    private IDBSession dbSession;

    private LearnerContentSummaryModel(String uid, String contentId) {
        this.uid = uid;
        this.contentId = contentId;
    }

    private LearnerContentSummaryModel(LearnerSummaryData learnerSummaryData) {
        this.uid = learnerSummaryData.getUid();
        this.contentId = learnerSummaryData.getContentId();
        this.timespent = learnerSummaryData.getTimespent();
        if ("2.0".equalsIgnoreCase(learnerSummaryData.getVer())) {
            this.timestamp = learnerSummaryData.getTimestamp();
        } else {
            this.timestamp = DateUtil.dateToEpoch(Long.toString(learnerSummaryData.getTimestamp()));
        }
    }

    private LearnerContentSummaryModel(IDBSession dbSession, LearnerSummaryData learnerSummaryData) {
        this(learnerSummaryData);
        this.dbSession = dbSession;
    }

    public static LearnerContentSummaryModel build(IDBSession dbSession, LearnerSummaryData learnerSummaryData) {
        return new LearnerContentSummaryModel(dbSession, learnerSummaryData);
    }

    public static LearnerContentSummaryModel findAssessmentById(IDBSession dbSession, String uid, String contentId) {
        LearnerContentSummaryModel learnerContentSummaryModel = new LearnerContentSummaryModel(uid, contentId);
        dbSession.read(learnerContentSummaryModel);

        return learnerContentSummaryModel;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            readWithoutMoving(cursor);
        }

        return this;
    }

    public IReadable readWithoutMoving(IResultSet cursor) {
        if (cursor.isAfterLast()) {
            this.avgts = this.timespent;
            this.sessions = 1;
            this.totalts = this.timespent;
            this.lastUpdated = this.timestamp;
        } else {
            this.id = cursor.getLong(0);
            this.uid = cursor.getString(1);
            this.contentId = cursor.getString(2);
            this.avgts = cursor.getDouble(3);
            this.sessions = cursor.getInt(4);
            this.totalts = cursor.getDouble(5);
            this.lastUpdated = cursor.getLong(6);
            update();
        }
        return this;
    }

    private void update() {
        if (this.timespent != null) {
            this.sessions++;
            this.totalts += this.timespent;
            this.avgts = this.totalts / this.sessions;
            this.lastUpdated = this.timestamp;
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_UID, this.uid);
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_AVG_TS, this.avgts);
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_SESSIONS, this.sessions);
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_TOTAL_TS, this.totalts);
        values.put(LearnerContentSummaryEntry.COLUMN_NAME_LAST_UPDATED_ON, this.lastUpdated);
        return values;
    }

    @Override
    public void updateId(long id) {

    }

    @Override
    public String getTableName() {
        return LearnerContentSummaryEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ? and %s = ? ", LearnerContentSummaryEntry.COLUMN_NAME_UID, LearnerContentSummaryEntry.COLUMN_NAME_CONTENT_ID);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{this.uid, this.contentId};
    }

    @Override
    public String limitBy() {
        return "";
    }

    public void save() {
        dbSession.create(this);
    }

}
