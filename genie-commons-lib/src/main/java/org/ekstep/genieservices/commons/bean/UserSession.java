package org.ekstep.genieservices.commons.bean;


import com.google.gson.Gson;

import org.ekstep.genieservices.commons.AppContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class UserSession {
    private String uid;
    private String sid;
    private String createdTime;

    public UserSession(String uid) {
        this.uid = uid;
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

    public Void save(AppContext appContext) {
        UserSession currentSession = getCurrentSession(this.toString());
        if (currentSession.getUid().equalsIgnoreCase(getUid())) {
            return null;
        }
        if (currentSession.isValid()) {
            endCurrentSession(appContext);
        }

        // TODO: 27/4/17 Need to add GESessionStart event
//        GESessionStart geSessionStart = new GESessionStart(this, location, deviceInfo.getDeviceID(), gameID, gameVersion);
//        Event event = new Event(geSessionStart.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionStart.toString());
//        event.save(dbOperator);
        return null;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean isValid() {
        return !uid.isEmpty();
    }

    public void endCurrentSession(AppContext appContext) {

        // TODO: 26/4/17 Add GESessionEnd event
//        GESessionEnd geSessionEnd = new GESessionEnd(gameID, gameVersion, currentSession, deviceInfo.getDeviceID());
//        Event event = new Event(geSessionEnd.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionEnd.toString());
//        event.save(dbOperator);
    }

    public UserSession getCurrentSession(String sessionJson) {
        if (sessionJson == null || sessionJson.isEmpty()) {
            return new UserSession(uid);
        }
        Gson gson = new Gson();
        UserSession userSession = gson.fromJson(sessionJson, UserSession.class);
        return userSession;
    }

}
