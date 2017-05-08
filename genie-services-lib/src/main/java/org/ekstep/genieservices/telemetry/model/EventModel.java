package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.exception.InvalidDataException;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.TelemetryEventPublisher;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryEntry;
import org.ekstep.genieservices.telemetry.taggers.IEventTagger;
import org.ekstep.genieservices.telemetry.taggers.PartnerTagger;
import org.ekstep.genieservices.telemetry.taggers.TelemetryTagger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by swayangjit on 26/4/17.
 */

public class EventModel implements IWritable {

    private static final String TAG = "model-Event";
    private Long id;
    private Map event;
    private String eventType;
    private PriorityModel priority;
    private String timestamp;
    private ContentValues contentValues;
    private List<IEventTagger> taggers = new ArrayList<>();
    private IDBSession mDBSession;


    private EventModel(IDBSession dbSession) {
        this(dbSession, null, null, new ContentValues());
        addTagger(new PartnerTagger());
    }

    private EventModel(IDBSession dbSession, Set<String> tags) {
        this(dbSession, null, null, new ContentValues());
        addTagger(new PartnerTagger());
        addTagger(new TelemetryTagger(tags));
    }

    private EventModel(IDBSession dbSession, String eventType, Set<String> hashedTags) {
        this(dbSession, eventType, PriorityModel.build(dbSession, eventType), new ContentValues());
        addTagger(new PartnerTagger());
        addTagger(new TelemetryTagger(hashedTags));
    }

    private EventModel(IDBSession dbSession, String eventType, PriorityModel priority, ContentValues contentValues) {
        this.mDBSession = dbSession;
        this.eventType = eventType;
        this.priority = priority;
        this.contentValues = contentValues;
        this.event = new HashMap();
    }

    public static EventModel build(IDBSession dbSession, IResultSet resultSet) {
        EventModel event = new EventModel(dbSession);
        event.id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        event.eventType = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT_TYPE));
        String eventString = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT));
        event.event = GsonUtil.fromJson(eventString, HashMap.class);
        event.timestamp = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_TIMESTAMP));
        event.priority = PriorityModel.build(dbSession, event.eventType, resultSet.getInt(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_PRIORITY)));
        return event;
    }

    public static EventModel build(IDBSession dbSession, String eventString, Set<String> activeTags) {
        EventModel event = new EventModel(dbSession, activeTags);
        event.event = GsonUtil.fromJson(eventString, Map.class, ServiceConstants.Event.ERROR_INVALID_EVENT);
        event.eventType = (String) event.event.get("eid");
        if (event.eventType == null || event.eventType.isEmpty()) {
            throw new InvalidDataException(ServiceConstants.Event.ERROR_INVALID_JSON);
        }
        event.priority = PriorityModel.build(dbSession, event.eventType);
        return event;
    }

//    public static Event build(IDBSession dbSession, String eventType, Set<String> hashedTags) {
//
//        return new Event(dbSession, eventType, hashedTags);
//    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(TelemetryEntry.COLUMN_NAME_EVENT, GsonUtil.toJson(event));
        contentValues.put(TelemetryEntry.COLUMN_NAME_EVENT_TYPE, eventType.toUpperCase());
        contentValues.put(TelemetryEntry.COLUMN_NAME_TIMESTAMP, new Date().getTime());
        contentValues.put(TelemetryEntry.COLUMN_NAME_PRIORITY, priority.getPriority());
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return TelemetryEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {
        tag(context);
        addMID();
    }

    public Long getId() {
        return id;
    }

    public EventModel addTagger(IEventTagger tagger) {
        this.taggers.add(tagger);
        return this;
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
//        Logger.i(mAppContext, TAG, "Priority added:" + priority.getPriority());
        this.mDBSession.create(this);
        TelemetryEventPublisher.postTelemetryEvent(this.mDBSession, this.event);
        return null;
    }

    public void addTag(String key, Object value) {
//        Logger.i(mAppContext, TAG, String.format("addTag %s:%s", key, value));
        Map<String, Object> _map = new HashMap<>();
        _map.put(key, value);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) this.event.get("tags");
        if (tags == null) {
//            Logger.i(mAppContext, TAG, String.format("CREATE TAG"));
            tags = new ArrayList<>();
            tags.add(_map);
            this.event.put("tags", tags);
        } else {
//            Logger.i(mAppContext, TAG, String.format("EDIT TAG"));
            tags.add(_map);
        }
    }

    private void tag(AppContext context) {
        Logger.i(TAG, "TAG");
        Iterator<IEventTagger> iterator = taggers.iterator();
        IEventTagger tagger;
        while (iterator.hasNext()) {
            tagger = iterator.next();
            Logger.i(TAG, String.format("TAGGER %s", tagger));
            tagger.tag(this, context);
        }

    }

    public int getPriority() {
        return this.priority.getPriority();
    }

    public String eventType() {
        return this.eventType;
    }

}
