package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GroupSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.UUID;

public class GroupSessionModel {

    private AppContext appContext;
    private GroupSession groupSessionBean;
    private String gid;

    private GroupSessionModel(AppContext appContext, String gid) {
        this.gid = gid;
        this.appContext = appContext;
    }

    public static GroupSessionModel buildUserSession(AppContext appContext, String gid) {
        GroupSessionModel groupSession = new GroupSessionModel(appContext, gid);
        return groupSession;
    }

    public static GroupSessionModel findGroupSession(AppContext appContext) {
        GroupSessionModel groupSessionModel = new GroupSessionModel(appContext, null);
        groupSessionModel.groupSessionBean = groupSessionModel.find();
        if (groupSessionModel.groupSessionBean == null) {
            return null;
        } else {
            return groupSessionModel;
        }
    }

    public GroupSession getGroupSessionBean() {
        return groupSessionBean;
    }

    public void startSession() {
        groupSessionBean = new GroupSession(gid, UUID.randomUUID().toString(), DateUtil.getCurrentTimestamp());
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_GROUP_SESSION, GsonUtil.toJson(groupSessionBean));
    }

    public void endSession() {
        //put the current group session to empty
        appContext.getKeyValueStore().putString(ServiceConstants.KEY_GROUP_SESSION, "");

    }

    public GroupSession find() {
        String sessionJson = appContext.getKeyValueStore().getString(ServiceConstants.KEY_GROUP_SESSION, "");
        if (sessionJson == null || sessionJson.isEmpty()) {
            return null;
        }
        GroupSession groupSession = GsonUtil.fromJson(sessionJson, GroupSession.class);
        return groupSession;
    }

}
