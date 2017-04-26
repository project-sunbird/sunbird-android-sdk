package org.ekstep.genieservices.telemetry.model;

import com.google.gson.Gson;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
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

public class Event implements IWritable {

    private static final String TAG = "model-Event";
    private Long id;
    private Map event;
    private String eventType;
    private Priority priority;
    private String timestamp;
    private ContentValues contentValues;
    private List<IEventTagger> taggers = new ArrayList<>();
    private AppContext mAppContext;


    private Event(AppContext appContext) {
        this(appContext, null, null, new ContentValues());
        addTagger(new PartnerTagger());
    }

    private Event(AppContext appContext, Set<String> tags) {
        this(appContext, null, null, new ContentValues());
        addTagger(new PartnerTagger());
        addTagger(new TelemetryTagger(tags));
    }

    private Event(AppContext appContext, String eventType, Set<String> hashedTags) {
        this(appContext, eventType, Priority.build(appContext, eventType), new ContentValues());
        addTagger(new PartnerTagger());
        addTagger(new TelemetryTagger(hashedTags));
    }

    private Event(AppContext appContext, String eventType, Priority priority, ContentValues contentValues) {
        this.mAppContext = appContext;
        this.eventType = eventType;
        this.priority = priority;
        this.contentValues = contentValues;
        event = new HashMap();
    }

    public static Event getForRead(AppContext appContext, IResultSet resultSet) {
        Event event = new Event(appContext);
        event.id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        event.eventType = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT_TYPE));
        String eventString = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_EVENT));
        event.event = new Gson().fromJson(eventString, HashMap.class);
        event.timestamp = resultSet.getString(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_TIMESTAMP));
        event.priority = Priority.build(appContext, event.eventType, resultSet.getInt(resultSet.getColumnIndex(TelemetryEntry.COLUMN_NAME_PRIORITY)));
        return event;
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(TelemetryEntry.COLUMN_NAME_EVENT, new Gson().toJson(event));
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

//    public static Event getForSave(String eventString, DeviceInfo deviceInfo, UserSession userSession,
//                                   DbOperator dbOperator, Context context) {
//        Event event = new Event(TelemetryTagCache.activeTags(dbOperator, context));
//        event.event = gsonWrapper.fromJson(eventString, Map.class, "invalid event");
//        event.eventType = (String) event.event.get("eid");
//        if (event.eventType == null || event.eventType.isEmpty())
//            throw new InvalidDataException("Invalid Json");
//        event.priority = new Priority(event.eventType);
//        event.updateEventDetails(deviceInfo);
//        event.updateSessionDetails(userSession);
//        return event;
//    }

    public Long getId() {
        return id;
    }

    public Event addTagger(IEventTagger tagger) {
        taggers.add(tagger);
        return this;
    }

//    public void updateSessionDetails(UserSession userSession) {
//        UserSession currentSession = userSession.getCurrentSession();
//        if (currentSession.isValid()) {
//            event.put("uid", currentSession.getUid());
//            event.put("sid", currentSession.getSid());
//        }
//    }
//
//    public void updateEventDetails(DeviceInfo deviceInfo) throws JsonSyntaxException {
//        event.put("did", deviceInfo.getDeviceID());
//        String ver = String.valueOf(event.get("ver"));
//        if (ver.equals("1.0")) {
//            String ts = (String) event.get("ts");
//            if (ts == null || ts.isEmpty())
//                event.put("ts", TimeUtil.getCurrentTimestamp());
//        } else if (ver.equals("2.0")) {
//            Double _ets;
//            try {
//                _ets = (Double) event.get("ets");
//            } catch (java.lang.ClassCastException e) {
//                _ets = null;
//            }
//            if (_ets == null) {
//                event.put("ets", TimeUtil.getEpochTime());
//            } else {
//                event.put("ets", Math.round(_ets));
//            }
//        }
//    }

    public Map getEventMap() {
        return event;
    }

    private void addMID() {
        event.put("mid", UUID.randomUUID().toString());
    }

    public Event withEvent(String eventString) {
        this.event = new Gson().fromJson(eventString, Map.class);
        return this;
    }

    public Void save() {
        priority = Priority.findByType(mAppContext, priority.getEventType());
        Logger.i(mAppContext, TAG, "Priority added:" + priority.getPriority());
        mAppContext.getDBSession().create(this);
        TelemetryEventPublisher.postTelemetryEvent(mAppContext, event);
        return null;
    }

    public void addTag(String key, Object value) {
        Logger.i(mAppContext, TAG, String.format("addTag %s:%s", key, value));
        Map<String, Object> _map = new HashMap<>();
        _map.put(key, value);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.get("tags");
        if (tags == null) {
            Logger.i(mAppContext, TAG, String.format("CREATE TAG"));
            tags = new ArrayList<>();
            tags.add(_map);
            event.put("tags", tags);
        } else {
            Logger.i(mAppContext, TAG, String.format("EDIT TAG"));
            tags.add(_map);
        }
    }

    private void tag(AppContext context) {
        Logger.i(context, TAG, "TAG");
        Iterator<IEventTagger> iterator = taggers.iterator();
        IEventTagger tagger;
        while (iterator.hasNext()) {
            tagger = iterator.next();
            Logger.i(context, TAG, String.format("TAGGER %s", tagger));
            tagger.tag(this, context);
        }

    }

    public int getPriority() {
        return priority.getPriority();
    }

    public String eventType() {
        return eventType;
    }

}
