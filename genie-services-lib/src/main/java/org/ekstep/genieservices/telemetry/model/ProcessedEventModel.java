package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryProcessedEntry;

import java.util.Locale;

/**
 * Created by swayangjit on 26/4/17.
 */

public class ProcessedEventModel implements IWritable, ICleanable, IReadable {

    private String msgId;
    private byte[] data;
    private Long id;
    private int numberOfEvents;
    private int priority;
    private ContentValues contentValues;
    private IDBSession mDBSession;

    private ProcessedEventModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.contentValues = new ContentValues();
    }

    private ProcessedEventModel(IDBSession dbSession, String msgId, byte[] data, int numberOfEvents, int priority) {
        this(dbSession, msgId, data, numberOfEvents, priority, new ContentValues());
    }

    private ProcessedEventModel(IDBSession dbSession, String msgId, byte[] data, int numberOfEvents, int priority, ContentValues contentValues) {
        this.mDBSession=dbSession;
        this.msgId = msgId;
        this.data = data;
        this.numberOfEvents = numberOfEvents;
        this.contentValues = contentValues;
        this.priority = priority;
    }

    public static ProcessedEventModel build(IDBSession dbSession) {
        return new ProcessedEventModel(dbSession);
    }

    public static ProcessedEventModel build(IDBSession dbSession, String msgId, byte[] data, int numberOfEvents, int priority) {
        return new ProcessedEventModel(dbSession, msgId, data, numberOfEvents, priority, new ContentValues());
    }



    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            readWithoutMoving(cursor);
        }
        return this;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = (select min(%s) from %s)", TelemetryProcessedEntry.COLUMN_NAME_PRIORITY, TelemetryProcessedEntry.COLUMN_NAME_PRIORITY, getTableName());
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return String.format(Locale.US, "limit %d", DbConstants.MAX_NUM_OF_PROCESSED_EVENTS);
    }

    @Override
    public void clean() {
        msgId = "";
        data = new byte[]{};
        id = null;
        numberOfEvents = 0;
    }

    @Override
    public String selectionToClean() {
        String selectionBy = String.format(Locale.US, "WHERE _id = %d", id);
        return selectionBy;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public void updateId(long id) {

    }

    @Override
    public String getTableName() {
        return TelemetryProcessedEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void find(IDBSession dbSesion) {
        clean();
        dbSesion.read(this);
    }

    public int clear() {
        int eventExported = this.numberOfEvents;
        mDBSession.clean(this);
        return eventExported;
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        msgId = resultSet.getString(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_MSG_ID));
        data = resultSet.getBlob(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_DATA));
        numberOfEvents = resultSet.getInt(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_NUMBER_OF_EVENTS));
        priority = resultSet.getInt(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_PRIORITY));
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return numberOfEvents == 0 || data == null || data.length == 0;
    }

}
