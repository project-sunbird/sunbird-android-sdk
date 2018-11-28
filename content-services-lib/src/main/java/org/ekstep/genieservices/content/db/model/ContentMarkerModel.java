package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentMarkerEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Locale;

public class ContentMarkerModel implements IWritable, IUpdatable, IReadable, ICleanable {

    private IDBSession mDBSession;
    private Long id = -1L;

    private String identifier;
    private String uid;
    private String data;
    private String visibility;
    private int marker;
    private Long epochTimestamp;

    private ContentMarkerModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private ContentMarkerModel(IDBSession dbSession, String uid, String identifier) {
        this.mDBSession = dbSession;
        this.uid = uid;
        this.identifier = identifier;
    }

    private ContentMarkerModel(IDBSession dbSession, String uid, String identifier, String data,
                               int marker, String visibility) {
        this.mDBSession = dbSession;
        this.uid = uid;
        this.identifier = identifier;
        this.data = data;
        this.marker = marker;
        this.visibility = visibility;
    }

    public static ContentMarkerModel build(IDBSession dbSession) {
        return new ContentMarkerModel(dbSession);
    }

    public static ContentMarkerModel build(IDBSession dbSession, String uid, String identifier,
                                           String data, int marker, String visibility) {
        return new ContentMarkerModel(dbSession, uid, identifier, data, marker, visibility);
    }

    public static ContentMarkerModel find(IDBSession dbSession, String uid, String identifier) {
        ContentMarkerModel contentMarkerModel = new ContentMarkerModel(dbSession, uid, identifier);
        dbSession.read(contentMarkerModel);

        if (contentMarkerModel.id == -1L) {
            return null;
        } else {
            return contentMarkerModel;
        }
    }

    public Void save() {
        mDBSession.create(this);
        return null;
    }

    public Void update() {
        mDBSession.update(this);
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        with(contentValues, ContentMarkerEntry.COLUMN_NAME_CONTENT_IDENTIFIER, identifier);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_UID, uid);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_DATA, data);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_VISIBILITY, visibility);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_MARKER, marker);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();

        with(contentValues, ContentMarkerEntry.COLUMN_NAME_DATA, data);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_VISIBILITY, visibility);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_MARKER, marker);
        with(contentValues, ContentMarkerEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());

        return contentValues;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    @Override
    public String getTableName() {
        return ContentMarkerEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        this.id = -1L;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = %s AND %s = %s",
                ContentMarkerEntry.COLUMN_NAME_UID, ContentMarkerEntry.COLUMN_NAME_CONTENT_IDENTIFIER, uid, identifier);
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", ContentMarkerEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ? AND %s = ?",
                ContentMarkerEntry.COLUMN_NAME_UID, ContentMarkerEntry.COLUMN_NAME_CONTENT_IDENTIFIER);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{uid, identifier};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s' AND %s = '%s'",
                ContentMarkerEntry.COLUMN_NAME_UID, uid, ContentMarkerEntry.COLUMN_NAME_CONTENT_IDENTIFIER, identifier);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        identifier = resultSet.getString(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_CONTENT_IDENTIFIER));
        uid = resultSet.getString(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_UID));
        data = resultSet.getString(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_DATA));
        visibility = resultSet.getString(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_VISIBILITY));
        marker = resultSet.getInt(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_MARKER));
        epochTimestamp = resultSet.getLong(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
    }

    private void with(ContentValues contentValues, String key, String value) {
        if (value != null) {
            contentValues.put(key, value);
        }
    }

    private void with(ContentValues contentValues, String key, int value) {
        contentValues.put(key, value);
    }

    private void with(ContentValues contentValues, String key, long value) {
        contentValues.put(key, value);
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }
}
