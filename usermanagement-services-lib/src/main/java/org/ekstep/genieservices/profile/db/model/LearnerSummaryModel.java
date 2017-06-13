package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.LearnerContentSummaryDetails;
import org.ekstep.genieservices.commons.db.contract.LearnerSummaryEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created on 5/6/17.
 * shriharsh
 */

public class LearnerSummaryModel implements IReadable, IWritable, IUpdatable {
    private Long id = -1L;
    private String uid;
    private String contentId;
    private Double avgts;
    private Integer sessions;
    private Double totalts;
    private Long lastUpdated;
    private Double timespent;
    private Long timestamp;
    private String hierarchyData;
    private IDBSession dbSession;

    private LearnerSummaryModel(IDBSession dbSession, String uid, String contentId, String hierarchyData) {
        this.uid = uid;
        this.contentId = contentId;
        this.hierarchyData = hierarchyData;
        this.dbSession = dbSession;
    }

    private LearnerSummaryModel(IDBSession dbSession, LearnerContentSummaryDetails learnerContentSummaryDetails) {
        this.uid = learnerContentSummaryDetails.getUid();
        this.contentId = learnerContentSummaryDetails.getContentId();
        this.avgts = learnerContentSummaryDetails.getAvgts();
        this.sessions = learnerContentSummaryDetails.getSessions();
        this.totalts = learnerContentSummaryDetails.getTotalts();
        this.lastUpdated = learnerContentSummaryDetails.getLastUpdated();
        this.timespent = learnerContentSummaryDetails.getTimespent();
        this.timestamp = learnerContentSummaryDetails.getTimestamp();
        this.hierarchyData = learnerContentSummaryDetails.getHierarchyData();
        this.dbSession = dbSession;
    }

    private LearnerSummaryModel(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    public static LearnerSummaryModel build(IDBSession dbSession, LearnerContentSummaryDetails learnerContentSummaryDetails) {
        return new LearnerSummaryModel(dbSession, learnerContentSummaryDetails);
    }

    public static LearnerSummaryModel find(IDBSession dbSession, String uid, String contentId, String hierarchyData) {
        LearnerSummaryModel model = new LearnerSummaryModel(dbSession, uid, contentId, hierarchyData);
        dbSession.read(model);

        if (model.id == -1L) {
            return null;
        } else {
            return model;
        }
    }

    public static LearnerSummaryModel build(IDBSession dbSession) {
        LearnerSummaryModel model = new LearnerSummaryModel(dbSession);
        return model;
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
        this.id = cursor.getLong(0);
        this.uid = cursor.getString(1);
        this.contentId = cursor.getString(2);
        this.avgts = cursor.getDouble(3);
        this.sessions = cursor.getInt(4);
        this.totalts = cursor.getDouble(5);
        this.lastUpdated = cursor.getLong(6);
        return this;
    }

    public void update() {
        dbSession.update(this);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LearnerSummaryEntry.COLUMN_NAME_UID, this.uid);
        values.put(LearnerSummaryEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        values.put(LearnerSummaryEntry.COLUMN_NAME_AVG_TS, this.avgts);
        values.put(LearnerSummaryEntry.COLUMN_NAME_SESSIONS, this.sessions);
        values.put(LearnerSummaryEntry.COLUMN_NAME_TOTAL_TS, this.totalts);
        values.put(LearnerSummaryEntry.COLUMN_NAME_LAST_UPDATED_ON, this.lastUpdated);
        values.put(LearnerSummaryEntry.COLUMN_NAME_HIERARCHY_DATA, this.hierarchyData);
        return values;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues values = new ContentValues();
        values.put(LearnerSummaryEntry.COLUMN_NAME_UID, this.uid);
        values.put(LearnerSummaryEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        values.put(LearnerSummaryEntry.COLUMN_NAME_AVG_TS, this.avgts);
        values.put(LearnerSummaryEntry.COLUMN_NAME_SESSIONS, this.sessions);
        values.put(LearnerSummaryEntry.COLUMN_NAME_TOTAL_TS, this.totalts);
        values.put(LearnerSummaryEntry.COLUMN_NAME_LAST_UPDATED_ON, this.lastUpdated);
        values.put(LearnerSummaryEntry.COLUMN_NAME_HIERARCHY_DATA, this.hierarchyData);
        return values;
    }

    @Override
    public String getTableName() {
        return LearnerSummaryEntry.TABLE_NAME;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "where %s = ? AND %s = ? AND %s = ? ", LearnerSummaryEntry.COLUMN_NAME_UID, LearnerSummaryEntry.COLUMN_NAME_CONTENT_ID,
                LearnerSummaryEntry.COLUMN_NAME_HIERARCHY_DATA);
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
        return String.format(Locale.US, "where %s = ? AND %s = ? AND %s = ? ", LearnerSummaryEntry.COLUMN_NAME_UID, LearnerSummaryEntry.COLUMN_NAME_CONTENT_ID,
                LearnerSummaryEntry.COLUMN_NAME_HIERARCHY_DATA);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{this.uid, this.contentId, this.hierarchyData};
    }

    @Override
    public String limitBy() {
        return "";
    }

    public void save() {
        dbSession.create(this);
    }

    public Double getAvgts() {
        return avgts;
    }

    public Integer getSessions() {
        return sessions;
    }

    public Double getTotalts() {
        return totalts;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public Double getTimespent() {
        return timespent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getId() {
        return this.id;
    }
}
