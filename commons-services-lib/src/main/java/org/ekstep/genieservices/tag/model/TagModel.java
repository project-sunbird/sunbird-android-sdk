package org.ekstep.genieservices.tag.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TagModel implements IReadable, IWritable, ICleanable, IUpdatable {

    private String name;
    private String hash;
    private String description;
    private String startDate;
    private String endDate;
    private Long id;
    private ContentValues contentValues;
    private IDBSession mDBSession;

    private TagModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.contentValues = new ContentValues();
    }

    private TagModel(IDBSession dbSession, String name) {
        this.mDBSession = dbSession;
        this.name = name;
        this.contentValues = new ContentValues();
    }

    private TagModel(IDBSession dbSession, String name, String hash, String description,
                     String startDate, String endDate) {
        this.mDBSession = dbSession;
        this.name = name;
        this.hash = hash;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contentValues = new ContentValues();
    }

    public static TagModel build(IDBSession dbSession) {
        return new TagModel(dbSession);
    }

    public static TagModel build(IDBSession dbSession, String tagName) {
        return new TagModel(dbSession, tagName);
    }

    public static TagModel build(IDBSession dbSession, String name, String hash, String description,
                                 String startDate, String endDate) {
        return new TagModel(dbSession, name, hash, description, startDate, endDate);
    }

    public static TagModel find(IDBSession dbSession, String tagName) {
        TagModel telemetryTag = new TagModel(dbSession, tagName);
        dbSession.read(telemetryTag);
        return telemetryTag.tagHash() != null ? telemetryTag : null;

    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(TelemetryTagEntry._ID, id);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_NAME, name);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_HASH, hash);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_START_DATE, startDate);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_END_DATE, endDate);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_NAME, name);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_START_DATE, startDate);
        contentValues.put(TelemetryTagEntry.COLUMN_NAME_END_DATE, endDate);
        return contentValues;
    }

    @Override
    public String getTableName() {
        return TelemetryTagEntry.TABLE_NAME;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", TelemetryTagEntry.COLUMN_NAME_NAME, name);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return TelemetryTagEntry.COLUMN_NAME_NAME;
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = %s", TelemetryTagEntry.COLUMN_NAME_NAME);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{name};
    }

    @Override
    public String limitBy() {
        return "";
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", TelemetryTagEntry.COLUMN_NAME_NAME, name);
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        name = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_NAME));
        hash = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_HASH));
        description = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_DESCRIPTION));
        startDate = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_START_DATE));
        endDate = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_END_DATE));
    }

    public void save() {
        mDBSession.create(this);
    }

    public void update() {
        mDBSession.update(this);
    }

    public void clear() {
        mDBSession.clean(this);
    }

    public String name() {
        return name;
    }

    public String tagHash() {
        return hash;
    }

    public String description() {
        return description;
    }

    public String startDate() {
        return startDate;
    }

    public String endDate() {
        return endDate;
    }
}
