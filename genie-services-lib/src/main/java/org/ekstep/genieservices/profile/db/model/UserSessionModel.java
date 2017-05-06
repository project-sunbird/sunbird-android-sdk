package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.UUID;

public class UserSessionModel {

    private AppContext appContext;
    private UserSession userSessionBean;
    private String uid;

    private UserSessionModel(AppContext appContext, String uid) {
        this.uid = uid;
        this.appContext = appContext;
    }

    public static UserSessionModel buildUserSession(AppContext appContext, String uid) {
        UserSessionModel userSession = new UserSessionModel(appContext, uid);
        return userSession;
    }

    public static UserSessionModel findUserSession(AppContext appContext) {
        UserSessionModel userSessionModel = new UserSessionModel(appContext, null);
        userSessionModel.userSessionBean = userSessionModel.find();
        if (userSessionModel.userSessionBean == null) {
            return null;
        } else {
            return userSessionModel;
        }
    }

    public UserSession getUserSessionBean() {
        return userSessionBean;
    }

    private void initSession(String uid) {
        userSessionBean = new UserSession(uid, UUID.randomUUID().toString(), DateUtil.getCurrentTimestamp());
    }

    public Void startSession() {
        initSession(uid);
        return null;
    }

    public void endSession() {
        //put the current user session to empty
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_USER_SESSION, "");

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
