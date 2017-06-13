package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class ProcessedEventModel implements IWritable, ICleanable, IReadable {

    private Long id = -1L;
    private IDBSession dbSession;
    private ContentValues contentValues;

    private String msgId;
    private byte[] data;
    private int numberOfEvents;
    private int priority;

    private ProcessedEventModel(IDBSession dbSession) {
        this.dbSession = dbSession;
        this.contentValues = new ContentValues();
    }

    private ProcessedEventModel(IDBSession dbSession, String msgId, byte[] data, int numberOfEvents, int priority) {
        this(dbSession);

        this.msgId = msgId;
        this.data = data;
        this.numberOfEvents = numberOfEvents;
        this.priority = priority;
    }

    public static ProcessedEventModel build(IDBSession dbSession) {
        return new ProcessedEventModel(dbSession);
    }

    public static ProcessedEventModel build(IDBSession dbSession, String msgId, byte[] data, int numberOfEvents, int priority) {
        return new ProcessedEventModel(dbSession, msgId, data, numberOfEvents, priority);
    }

    public static ProcessedEventModel find(IDBSession dbSesion) {
        ProcessedEventModel processedEventModel = new ProcessedEventModel(dbSesion);
        dbSesion.read(processedEventModel);
        return processedEventModel;
    }

    public void save() {
        dbSession.create(this);
    }

    public int delete() {
        int eventExported = this.numberOfEvents;
        dbSession.clean(this);
        return eventExported;
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
        id = -1L;
        msgId = "";
        data = new byte[]{};
        numberOfEvents = 0;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE _id = %d", id);
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.put(TelemetryProcessedEntry.COLUMN_NAME_MSG_ID, msgId);
        contentValues.put(TelemetryProcessedEntry.COLUMN_NAME_DATA, data);
        contentValues.put(TelemetryProcessedEntry.COLUMN_NAME_NUMBER_OF_EVENTS, numberOfEvents);
        contentValues.put(TelemetryProcessedEntry.COLUMN_NAME_PRIORITY, priority);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return TelemetryProcessedEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        msgId = resultSet.getString(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_MSG_ID));
        data = resultSet.getBlob(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_DATA));
        numberOfEvents = resultSet.getInt(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_NUMBER_OF_EVENTS));
        priority = resultSet.getInt(resultSet.getColumnIndex(TelemetryProcessedEntry.COLUMN_NAME_PRIORITY));
    }

    public String getMsgId() {
        return msgId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public int getPriority() {
        return priority;
    }

}