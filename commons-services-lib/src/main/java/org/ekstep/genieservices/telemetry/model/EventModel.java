package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by swayangjit on 26/4/17.
 */

public class EventModel implements IWritable,IReadable {

    private static final String TAG = "model-Event";
    private Long id;
    private Map event;
    private String eventType;
    private PriorityModel priority;
    private String timestamp;
    private ContentValues contentValues;
    private IDBSession mDBSession;


    private EventModel(IDBSession dbSession) {
        this(dbSession, null, null, new ContentValues());
    }

    private EventModel(IDBSession dbSession, String eventType) {
        this(dbSession, eventType, PriorityModel.build(dbSession, eventType), new ContentValues());
    }

    private EventModel(IDBSession dbSession, String eventType, PriorityModel priority, ContentValues contentValues) {
        this.mDBSession = dbSession;
        this.eventType = eventType;
        this.priority = priority;
        this.contentValues = contentValues;
        this.event = new HashMap();
    }

    public static EventModel build(IDBSession dbSession) {
        return new EventModel(dbSession);
    }

    public static EventModel build(IDBSession dbSession, String eventString) {
        EventModel event = new EventModel(dbSession);
        event.event = GsonUtil.fromJson(eventString, Map.class, ServiceConstants.Event.ERROR_INVALID_EVENT);
        event.eventType = (String) event.event.get("eid");
        if (event.eventType == null || event.eventType.isEmpty()) {
            throw new InvalidDataException(ServiceConstants.Event.ERROR_INVALID_JSON);
        }
        event.priority = PriorityModel.build(dbSession, event.eventType);
        return event;
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
        timestamp = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_TIMESTAMP));
        priority = PriorityModel.build(mDBSession, eventType, resultSet.getInt(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_PRIORITY)));
    }

    public Long getId() {
        return id;
    }

    public void updateSessionDetails(String sid, String uid) {
        this.event.put("uid", uid);
        this.event.put("sid", sid);
    }

    public void updateTs(String timestamp) {
        String ts = (String) event.get("ts");
        if (ts == null || ts.isEmpty()) {
            this.event.put("ts", timestamp);
        }

    }

    public void updateEts(long ets) {
        Double _ets;
        try {
            _ets = (Double) event.get("ets");
        } catch (java.lang.ClassCastException e) {
            _ets = null;
        }
        if (_ets == null) {
            this.event.put("ets", ets);
        } else {
            this.event.put("ets", Math.round(_ets));
        }
    }

    public void updateDeviceInfo(String did) {
        this.event.put("did", did);

    }

    public Map getEventMap() {
        return this.event;
    }

    public String getVersion() {
        return String.valueOf(this.event.get("ver"));
    }

    private void addMID() {
        this.event.put("mid", UUID.randomUUID().toString());
    }

    public EventModel withEvent(String eventString) {
        this.event = GsonUtil.fromJson(eventString, Map.class);
        return this;
    }

    public Void save() {
        this.priority = PriorityModel.findByType(this.mDBSession, this.priority.getEventType());
        this.mDBSession.create(this);

        return null;
    }

    public void addTag(String key, Object value) {
        Logger.i(TAG, String.format("addTag %s:%s", key, value));
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) this.event.get("tags");
        if (tags == null) {
            Logger.i(TAG, String.format("CREATE TAG"));
            tags = new ArrayList<>();
            tags.add(map);
            this.event.put("tags", tags);
        } else {
            Logger.i(TAG, String.format("EDIT TAG"));
            tags.add(map);
        }
    }


    public int getPriority() {
        return this.priority.getPriority();
    }

    public String eventType() {
        return this.eventType;
    }

}