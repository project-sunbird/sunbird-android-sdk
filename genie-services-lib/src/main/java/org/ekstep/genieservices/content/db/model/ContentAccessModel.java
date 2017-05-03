package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.db.contract.ContentAccessEntry;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 1/25/2017.
 *
 * @author anil
 */
public class ContentAccessModel implements IWritable, IReadable, IUpdatable {
    private Long id = -1L;

    private String uid;
    private String identifier;
    private Long epochTimestamp;
    private int status;
    private String contentType;
    private Map<String, Object> learnerState;
    private AppContext appContext;

    private ContentAccessModel(AppContext appContext) {
        this.appContext = appContext;
    }

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

    public static ContentAccessModel buildContentAccess(AppContext appContext) {
        return new ContentAccessModel(appContext);
    }

    public static ContentAccessModel buildContentAccess(AppContext appContext, String uid, String identifier, int status, String contentType) {
        ContentAccessModel contentAccess = new ContentAccessModel(appContext, uid, identifier, status, contentType);
        return contentAccess;
    }

    public static ContentAccessModel find(AppContext appContext, String uid, String identifier) {
        ContentAccessModel contentAccess = new ContentAccessModel(appContext, uid, identifier);
        appContext.getDBSession().read(contentAccess);

        if (contentAccess.getId() == -1) {
            return null;
        } else {
            return contentAccess;
        }
    }

    public void save(AppContext appContext) {
        appContext.getDBSession().create(ContentAccessModel.this);
    }

    public void update(AppContext appContext) {
        appContext.getDBSession().update(ContentAccessModel.this);
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
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
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

    public void readWithoutMoving(IResultSet cursor) {
        id = cursor.getLong(0);
        uid = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_UID));
        identifier = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_IDENTIFIER));
        epochTimestamp = cursor.getLong(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
        status = cursor.getInt(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_STATUS));
        contentType = cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE));
        learnerState = GsonUtil.fromJson(cursor.getString(cursor.getColumnIndex(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE)), Map.class);
    }

    public Long getId() {
        return id;
    }

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

}
