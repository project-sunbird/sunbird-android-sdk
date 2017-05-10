package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryTagModel implements IReadable, IWritable, ICleanable {

    private String name;
    private String hash;
    private String description;
    private String startDate;
    private String endDate;
    private Long id;
    private ContentValues contentValues;
    private IDBSession mDBSession;

    private TelemetryTagModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.contentValues = new ContentValues();
    }

    private TelemetryTagModel(IDBSession dbSession, String name, String hash, String description,
                         String startDate, String endDate) {
        this.mDBSession = dbSession;
        this.name = name;
        this.hash = hash;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contentValues = new ContentValues();
    }

    public static TelemetryTagModel build(IDBSession dbSession, String name, String hash, String description,
                                     String startDate, String endDate) {
        return new TelemetryTagModel(dbSession, name, hash, description, startDate, endDate);
    }

    public static TelemetryTagModel find(IDBSession dbSession) {
        TelemetryTagModel telemetryTag = new TelemetryTagModel(dbSession);
        dbSession.read(telemetryTag);
        return telemetryTag;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
            name = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_NAME));
            hash = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_HASH));
            description = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_DESCRIPTION));
            startDate = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_START_DATE));
            endDate = resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_END_DATE));
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
    public String getTableName() {
        return TelemetryTagEntry.TABLE_NAME;
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
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
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
        return "";
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
