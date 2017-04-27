package org.ekstep.genieservices.content.db.model;


import com.google.gson.Gson;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.content.db.contract.ContentAccessEntry;

import java.util.Locale;
import java.util.Map;


/**
 * Created on 1/25/2017.
 *
 * @author anil
 */
public class ContentAccessModel implements IWritable, ICleanable, IReadable, IUpdatable {
    private Long id = -1L;

    private String uid;
    private String identifier;
    private Long epochTimestamp;
    private int status;
    private String contentType;
    private Map<String, Object> learnerState;
    private AppContext appContext;

    private ContentAccessModel() {
    }

    // TODO: Remove this and setter for uid and identifier
    private ContentAccessModel(AppContext appContext, String uid, String identifier) {
        this(appContext, uid, identifier, ServiceConstants.ACCESS_STATUS_VIEWED, null);
    }

    private ContentAccessModel(AppContext appContext, String uid, String identifier, int status, String contentType) {
        this.uid = uid;
        this.identifier = identifier;
        this.status = status;
        this.contentType = contentType;
        this.appContext = appContext;
    }

    private ContentAccessModel(String identifier) {
        this.identifier = identifier;
    }

    public static ContentAccessModel buildContentAccess(AppContext appContext, String uid, String identifier) {
        ContentAccessModel contentAccess = new ContentAccessModel(appContext, uid, identifier);
        return contentAccess;
    }

    public static ContentAccessModel find(AppContext appContext, String uid, String identifier) {
        ContentAccessModel contentAccess = new ContentAccessModel(appContext, uid, identifier);
        appContext.getDBSession().read(contentAccess);
        return contentAccess;
    }

//    public boolean exists(DbOperator dbOperator) {
//        ContentAccess otherContentAccess = new ContentAccess(uid, identifier);
//        Reader otherContentAccessReader = new Reader(otherContentAccess);
//        dbOperator.execute(otherContentAccessReader);
//        return otherContentAccess.id != -1L;
//    }

//    public void save(DbOperator dbOperator) {
//        dbOperator.execute(new Writer(this));
//    }

//    public void update(DbOperator dbOperator) {
//        dbOperator.execute(new Updater(this));
//    }

    public String getIdentifier() {
        return identifier;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(Map<String, Object> learnerState) {
        this.learnerState = learnerState;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentAccessEntry.COLUMN_NAME_UID, this.uid);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_IDENTIFIER, this.identifier);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());
        contentValues.put(ContentAccessEntry.COLUMN_NAME_STATUS, this.status);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, this.contentType);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE, this.learnerState);

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            readWithoutMoving(cursor);
        }

        return this;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentAccessEntry.COLUMN_NAME_UID, this.uid);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_IDENTIFIER, this.identifier);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());
        contentValues.put(ContentAccessEntry.COLUMN_NAME_STATUS, this.status);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, this.contentType);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE, this.learnerState);

        return contentValues;
    }

    @Override
    public String getTableName() {
        return ContentAccessEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s' AND %s = '%s'", ContentAccessEntry.COLUMN_NAME_UID, uid, ContentAccessEntry.COLUMN_NAME_IDENTIFIER, identifier);
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
    }

    @Override
    public String filterForRead() {
        if (uid != null) {
            return String.format(Locale.US, "where %s = ? AND %s = ?", ContentAccessEntry.COLUMN_NAME_UID, ContentAccessEntry.COLUMN_NAME_IDENTIFIER);
        } else {
            return String.format(Locale.US, "where %s = ?", ContentAccessEntry.COLUMN_NAME_IDENTIFIER);
        }

    }

    @Override
    public String[] selectionArgsForFilter() {
        if (uid != null) {
            return new String[]{uid, identifier};
        } else {
            return new String[]{identifier};
        }


    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public void clean() {
        this.id = -1L;
    }

    @Override
    public String selectionToClean() {
        // Delete all row by uid
        return String.format(Locale.US, "where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_UID, uid);
    }

    /**
     * Delete all row by content identifier.
     */
//    public void deleteByIdentifier() {
//        String selectionToClean = String.format(Locale.US, "where %s = '%s' ", ContentAccessEntry.COLUMN_NAME_IDENTIFIER, identifier);
//        String query = String.format(Locale.US, "DELETE FROM %s %s", ContentAccessEntry.TABLE_NAME, selectionToClean);
//
//        CustomQuery customQuery = new CustomQuery(query);
//        dbOperator.execute(customQuery);
//    }
    public void readWithoutMoving(IResultSet cursor) {
        id = cursor.getLong(0);
        uid = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_UID));
        identifier = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_IDENTIFIER));
        epochTimestamp = cursor.getLong(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
        status = cursor.getInt(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_STATUS));
        contentType = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE));
        learnerState = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE)), Map.class);
    }

}
