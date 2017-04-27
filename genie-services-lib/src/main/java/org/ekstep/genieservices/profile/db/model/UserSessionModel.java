package org.ekstep.genieservices.profile.db.model;


import com.google.gson.Gson;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.UUID;

public class UserSessionModel {

    private AppContext appContext;
    private UserSession userSessionBean;
    private String uid;

    private UserSessionModel(AppContext appContext, String uid) {
        this.uid = uid;
        this.appContext = appContext;
        userSessionBean = new UserSession(uid, UUID.randomUUID().toString(), getCreatedDate());
    }

    public static UserSessionModel buildUserSession(AppContext appContext, String uid) {
        UserSessionModel userSession = new UserSessionModel(appContext, uid);
        return userSession;
    }

    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
    }

    private String getCreatedDate() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = getDateTimeFormatter();
        return fmt.print(dt);
    }

    public Void save() {
        UserSession currentSession = find();
        if (currentSession.getUid().equalsIgnoreCase(userSessionBean.getUid())) {
            return null;
        }
        if (isValid()) {
            endCurrentSession();
        }

        //save the session in shared pref
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_USER_SESSION, userSessionBean.toString());

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

    public void endCurrentSession() {
        //put the current user session to empty
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_USER_SESSION, "");

        // TODO: 26/4/17 Add GESessionEnd event
//        GESessionEnd geSessionEnd = new GESessionEnd(gameID, gameVersion, currentSession, deviceInfo.getDeviceID());
//        Event event = new Event(geSessionEnd.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionEnd.toString());
//        event.save(dbOperator);
    }

    public UserSession find() {
        String sessionJson = appContext.getKeyValueStore().getString(ServiceConstants.KEY_USER_SESSION, "");

        if (sessionJson == null || sessionJson.isEmpty()) {
            return userSessionBean;
        }

        UserSession userSession = GsonUtil.fromJson(sessionJson, UserSession.class);
        return userSession;
    }

}
