package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Locale;

/**
 * Created on 1/25/2017.
 *
 * @author anil
 */
public class ContentAccessModel implements IWritable, IReadable, IUpdatable {

    private IDBSession mDBSession;

    private Long id = -1L;
    private String uid;
    private String identifier;
    private int status;
    private String contentType;
    private String learnerStateJson;
    private Long epochTimestamp;

    private ContentAccessModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private ContentAccessModel(IDBSession dbSession, String uid, String identifier) {
        this.mDBSession = dbSession;
        this.uid = uid;
        this.identifier = identifier;
    }

    private ContentAccessModel(IDBSession dbSession, String uid, String identifier, String contentType, String learnerStateJson) {
        this(dbSession, uid, identifier);
        this.contentType = contentType;
        this.learnerStateJson = learnerStateJson;
    }

    private ContentAccessModel(IDBSession dbSession, String uid, String identifier, int status, String contentType) {
        this(dbSession, uid, identifier);
        this.status = status;
        this.contentType = contentType;
    }

    public static ContentAccessModel build(IDBSession dbSession) {
        return new ContentAccessModel(dbSession);
    }

    public static ContentAccessModel build(IDBSession dbSession, String uid, String identifier, int status, String contentType) {
        ContentAccessModel contentAccess = new ContentAccessModel(dbSession, uid, identifier, status, contentType);
        return contentAccess;
    }

    public static ContentAccessModel build(IDBSession dbSession, String uid, String identifier, String contentType, String learnerStateJson) {
        ContentAccessModel contentAccess = new ContentAccessModel(dbSession, uid, identifier, contentType, learnerStateJson);
        return contentAccess;
    }

    public static ContentAccessModel find(IDBSession dbSession, String uid, String identifier) {
        ContentAccessModel contentAccess = new ContentAccessModel(dbSession, uid, identifier);
        dbSession.read(contentAccess);

        if (contentAccess.getId() == -1) {
            return null;
        } else {
            return contentAccess;
        }
    }

    public void save() {
        mDBSession.create(this);
    }

    public void update() {
        mDBSession.update(this);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContentAccessEntry.COLUMN_NAME_UID, this.uid);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, this.identifier);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());
        contentValues.put(ContentAccessEntry.COLUMN_NAME_STATUS, this.status);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, this.contentType);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE, this.learnerStateJson);

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
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, this.identifier);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());
        contentValues.put(ContentAccessEntry.COLUMN_NAME_STATUS, this.status);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, this.contentType);
        contentValues.put(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE, this.learnerStateJson);

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
        return String.format(Locale.US, "%s = '%s' AND %s = '%s'", ContentAccessEntry.COLUMN_NAME_UID, uid, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, identifier);
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ? AND %s = ?", ContentAccessEntry.COLUMN_NAME_UID, ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{uid, identifier};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        uid = resultSet.getString(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_UID));
        identifier = resultSet.getString(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER));
        epochTimestamp = resultSet.getLong(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
        status = resultSet.getInt(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_STATUS));
        contentType = resultSet.getString(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE));
        learnerStateJson = resultSet.getString(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_LEARNER_STATE));
    }

    public Long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getUid() {
        return uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLearnerStateJson() {
        return learnerStateJson;
    }

    public void setLearnerStateJson(String learnerStateJson) {
        this.learnerStateJson = learnerStateJson;
    }

}
