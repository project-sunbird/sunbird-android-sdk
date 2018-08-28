package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class EventModel implements IWritable, IReadable {

    private static final String TAG = "model-Event";

    private Long id;
    private IDBSession mDBSession;
    private Map<String, Object> event;
    private String eventType;
    private PriorityModel priority;
    private ContentValues contentValues;

    private EventModel(IDBSession dbSession) {
        this(dbSession, new HashMap<String, Object>(), null, null);
    }

    private EventModel(IDBSession dbSession, Map<String, Object> event, String eventType, PriorityModel priority) {
        this.mDBSession = dbSession;
        this.event = event;
        this.eventType = eventType;
        this.priority = priority;
        this.contentValues = new ContentValues();
    }

    public static EventModel build(IDBSession dbSession) {
        return new EventModel(dbSession);
    }

    public static EventModel build(IDBSession dbSession, Map<String, Object> event, String eventType) {
        EventModel eventModel = new EventModel(dbSession, event, eventType, PriorityModel.build(dbSession, eventType));
        return eventModel;
    }

    public Void save() {
        this.priority = PriorityModel.findByType(this.mDBSession, this.priority.getEventType());
        this.mDBSession.create(this);
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(TelemetryEntry.COLUMN_NAME_EVENT, GsonUtil.toJson(event));
        contentValues.put(TelemetryEntry.COLUMN_NAME_EVENT_TYPE, eventType.toUpperCase());
        contentValues.put(TelemetryEntry.COLUMN_NAME_TIMESTAMP, DateUtil.getEpochTime());
        contentValues.put(TelemetryEntry.COLUMN_NAME_PRIORITY, priority.getPriority());
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
    public String getTableName() {
        return TelemetryEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
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
    public void beforeWrite(AppContext context) {
        addMID();
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        eventType = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT_TYPE));
        String eventString = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT));
        event = GsonUtil.fromJson(eventString, HashMap.class);
//        timestamp = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_TIMESTAMP));
        priority = PriorityModel.build(mDBSession, eventType, resultSet.getInt(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_PRIORITY)));
    }

    public Long getId() {
        return id;
    }

    // TODO: 7/12/2017 - Delete this
    public Map getEventMap() {
        return this.event;
    }

    // TODO: 7/12/2017 - Move this to TelemetrySeriveImpl
    private void addMID() {
        this.event.put("mid", UUID.randomUUID().toString());
    }

    public int getPriority() {
        return this.priority.getPriority();
    }

}