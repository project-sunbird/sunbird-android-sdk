package org.ekstep.genieservices.profile.db.model;


import com.google.gson.Gson;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Base64;
import java.util.UUID;

public class UserSessionModel {

    private AppContext appContext;
    private UserSession userSessionBean;
    private String uid;

    private UserSessionModel(AppContext appContext, String uid) {
        this.uid = uid;
        this.appContext = appContext;
    }

    private void initSession(String uid) {
        userSessionBean = new UserSession(uid, UUID.randomUUID().toString(), DateUtil.getCurrentTimestamp());
    }

    public static UserSessionModel buildUserSession(AppContext appContext, String uid) {
        UserSessionModel userSession = new UserSessionModel(appContext, uid);
        userSession.initSession(uid);
        return userSession;
    }

    public static UserSessionModel findUserSession(AppContext appContext, String uid) {
        UserSessionModel userSession = new UserSessionModel(appContext, uid);
        return userSession;
    }

    public Void save() {
        UserSession currentSession = find();
        if (currentSession != null && currentSession.getUid().equalsIgnoreCase(userSessionBean.getUid())) {
            return null;
        }
        if (!uid.isEmpty()) {
            // TODO: End session should be called from service and not inside the model
            endCurrentSession();
        }
        //save the session in shared pref
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_USER_SESSION, GsonUtil.toJson(userSessionBean));

        // TODO: 27/4/17 Need to add GESessionStart event
//        GESessionStart geSessionStart = new GESessionStart(this, location, deviceInfo.getDeviceID(), gameID, gameVersion);
//        Event event = new Event(geSessionStart.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionStart.toString());
//        event.save(dbOperator);
        return null;
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
            return null;
        }
        UserSession userSession = GsonUtil.fromJson(sessionJson, UserSession.class);
        return userSession;
    }

}
