package org.ekstep.genieservices.commons.bean;


import com.google.gson.Gson;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.UUID;

public class UserSession {
    private transient AppContext context;
    private String uid;
    private String sid;
    private String createdTime;
    private transient IKeyValueStore sharedPref;

    public UserSession(AppContext context, String uid) {
        this(context, uid, context.getKeyValueStore());
    }

    UserSession(AppContext context, String uid, IKeyValueStore sharedPref) {
        this.context = context;
        this.uid = uid;
        this.sharedPref = sharedPref;
        this.sid = UUID.randomUUID().toString();
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = getDateTimeFormatter();
        createdTime = fmt.print(dt);
    }

    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
    }

    public String getUid() {
        return uid;
    }

    public String getSid() {
        return sid;
    }

    public DateTime getCreatedTime() {
        return getDateTimeFormatter().parseDateTime(createdTime);
    }

    public Void save(String gameID, String gameVersion, String location, IDeviceInfo deviceInfo) {
        UserSession currentSession = getCurrentSession();
        if (currentSession.getUid().equalsIgnoreCase(getUid())) {
            return null;
        }
        if (currentSession.isValid()) {
            endCurrentSession(gameID, gameVersion, currentSession, deviceInfo);
        }
        saveSession();
//        GESessionStart geSessionStart = new GESessionStart(this, location, deviceInfo.getDeviceID(), gameID, gameVersion);
//        Event event = new Event(geSessionStart.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionStart.toString());
//        event.save(dbOperator);
        return null;
    }

    private void saveSession() {
        String sessionJson = this.toString();
        sharedPref.putString("session", sessionJson);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean isValid() {
        return !uid.isEmpty();
    }

    public void endCurrentSession(String gameID, String gameVersion, UserSession currentSession,
                                  IDeviceInfo deviceInfo) {
        sharedPref.putString("session", "");
//        GESessionEnd geSessionEnd = new GESessionEnd(gameID, gameVersion, currentSession, deviceInfo.getDeviceID());
//        Event event = new Event(geSessionEnd.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionEnd.toString());
//        event.save(dbOperator);
    }

    public UserSession getCurrentSession() {
        String sessionJson = sharedPref.getString("session", "");
        if (sessionJson == null || sessionJson.isEmpty()) {
            return new UserSession(context, "", sharedPref);
        }
        Gson gson = new Gson();
        UserSession userSession = gson.fromJson(sessionJson, UserSession.class);
        return userSession;
    }

    public AppContext context() {
        return context;
    }
}
