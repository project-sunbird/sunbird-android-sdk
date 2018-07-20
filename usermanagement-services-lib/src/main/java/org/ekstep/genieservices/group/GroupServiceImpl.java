package org.ekstep.genieservices.group;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IGroupService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.AddUpdateProfilesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.bean.telemetry.Actor;
import org.ekstep.genieservices.commons.bean.telemetry.Audit;
import org.ekstep.genieservices.commons.bean.telemetry.Error;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.db.model.GroupModel;
import org.ekstep.genieservices.profile.db.model.GroupProfileModel;
import org.ekstep.genieservices.profile.db.model.GroupProfilesModel;
import org.ekstep.genieservices.profile.db.model.GroupsModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Any service related to Group will be called using GroupServiceImpl
 * <p>
 * Created by swayangjit on 13/7/18.
 */
public class GroupServiceImpl extends BaseService implements IGroupService {

    private static final String TAG = GroupServiceImpl.class.getSimpleName();

    // TODO: 20/7/18 SHOULD BE CHANGED AFTER MAKING THE CHANGES WRT SESSION
    private Group dummyGroup = null;

    public GroupServiceImpl(AppContext appContext) {
        super(appContext);
    }

    /**
     * Creates a new group
     *
     * @param group - {@link Group}
     * @return
     */
    @Override
    public GenieResponse<Group> createGroup(Group group) {
        String methodName = "createGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Group> response;
        if (group == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_GROUP, ServiceConstants.ErrorMessage.INVALID_GROUP, methodName, Group.class);
            logGEError(response, "create-group-profile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_CREATE_GROUP);
            return response;
        } else {
            response = saveGroup(group, mAppContext.getDBSession());
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        }
    }

    private GenieResponse<Group> saveGroup(final Group group, IDBSession dbSession) {
        String gid = group.getGid();
        if (StringUtil.isNullOrEmpty(gid)) {
            gid = UUID.randomUUID().toString();
        }

        group.setGid(gid);

        if (group.getCreatedAt() == null) {
            group.setCreatedAt(DateUtil.getEpochTime());
        }
        final GroupModel groupModel = GroupModel.build(dbSession, group);

        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                groupModel.save();
                logGroupAuditEvent(groupModel.getGroup().getGid());
                return null;
            }
        });

        GenieResponse<Group> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        successResponse.setResult(groupModel.getGroup());
        return successResponse;
    }


    private void logGroupAuditEvent(String uid) {
        Audit.Builder audit = new Audit.Builder();
        audit.currentState(ServiceConstants.Telemetry.AUDIT_CREATED)
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_USER)
                .objectId(uid)
                .actorType(Actor.TYPE_SYSTEM).actorId(mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(audit.build());
    }

//    private void logProfileAuditEvent(Group group, Group updatedGroup) {
//
//        Audit.Builder audit = new Audit.Builder();
//        audit.currentState(updatedGroup == null ? ServiceConstants.Telemetry.AUDIT_CREATED : ServiceConstants.Telemetry.AUDIT_UPDATED)
//                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
//                .updatedProperties(updatedGroup == null ? findAvailableProps(group) : findProfilePropDiff(group, updatedGroup))
//                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_USER)
//                .objectId(group.getGid())
//                .actorType(Actor.TYPE_SYSTEM).actorId(mAppContext.getDeviceInfo().getDeviceID());
//        TelemetryLogger.log(audit.build());
//    }


    private void logGEError(GenieResponse response, String id) {
        Error.Builder error = new Error.Builder();
        error.errorCode(response.getError())
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .errorType(Error.Type.MOBILE_APP)
                .stacktrace(response.getErrorMessages().toString())
                .pageId(id);
        TelemetryLogger.log(error.build());
    }

    @Override
    public GenieResponse<Group> updateGroup(Group group) {
        String methodName = "updateGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Group> response;
        if (group == null || StringUtil.isNullOrEmpty(group.getGid())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.GROUP_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_GROUP, TAG, Group.class);
            logGEError(response, "update-group");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_GROUP);
            return response;
        }

        GroupModel groupDBModel = GroupModel.findGroupById(mAppContext.getDBSession(), group.getGid());
        if (groupDBModel != null) {
            GroupModel groupModel = GroupModel.build(mAppContext.getDBSession(), group);
            groupModel.update();

            // TODO: 16/7/18 Need to add telemetry part here
//            logGroupAuditEvent();

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Group.class);
            response.setResult(group);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.GROUP_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_GROUP, TAG, Group.class);
            logGEError(response, "delete-group");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_GROUP);
            return response;
        }
    }

    @Override
    public GenieResponse<List<Group>> getAllGroup() {
        String methodName = "getAllGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "1");

        GenieResponse<List<Group>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        GroupsModel groupsModel = GroupsModel.find(mAppContext.getDBSession());
        if (groupsModel == null) {
            response.setResult(new ArrayList<Group>());
        } else {
            response.setResult(groupsModel.getGroupList());
        }
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> deleteGroup(String gid) {
        String methodName = "deleteGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("gid", gid);
        params.put("logLevel", "2");

        GenieResponse<Void> response;
        if (StringUtil.isNullOrEmpty(gid)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.GROUP_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_GROUP, TAG, Void.class);
            logGEError(response, "delete-group");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_GROUP);
            return response;
        }

        GroupModel groupDBModel = GroupModel.findGroupById(mAppContext.getDBSession(), gid);
        if (groupDBModel != null) {
            groupDBModel.delete();

            //Deleting from the GroupProfilesModel, mapping between group and profile, when the group does not exist
            //all its related mapping has to be removed
            GroupProfilesModel groupProfilesModel = GroupProfilesModel.findByGid(mAppContext.getDBSession(), gid);
            if (groupProfilesModel != null) {
                groupProfilesModel.delete();
            }

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.GROUP_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_GROUP, TAG, Void.class);
            logGEError(response, "delete-group");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_GROUP);
            return response;
        }
    }

    @Override
    public GenieResponse<Void> setCurrentGroup(String gid) {
        String methodName = "setCurrentGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("gid", gid);
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        GroupModel groupModel = GroupModel.findGroupById(mAppContext.getDBSession(), gid);
        if (groupModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_GROUP, ServiceConstants.ErrorMessage.NO_GROUP_WITH_SPECIFIED_ID, TAG, Void.class);
            logGEError(response, "setCurrentGroup");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_SET_CURRENT_GROUP);
            return response;
        } else {
            dummyGroup = groupModel.getGroup();
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
            return response;
        }

    }

    @Override
    public GenieResponse<Group> getCurrentGroup() {
        String methodName = "getCurrentGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "1");

        GenieResponse<Group> response;
        if (dummyGroup == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_GROUP, ServiceConstants.ErrorMessage.NO_GROUP_WITH_SPECIFIED_ID, TAG, Group.class);
        } else {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Group.class);
            response.setResult(dummyGroup);
        }

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }


    @Override
    public GenieResponse<Void> addUpdateProfilesToGroup(AddUpdateProfilesRequest addUpdateProfilesRequest) {
        String methodName = "deleteGroup@GroupServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        String groupId = addUpdateProfilesRequest.getGroupId();

        //iterate on the list of users
        GroupProfilesModel groupProfilesDBModel = GroupProfilesModel.findByGid(mAppContext.getDBSession(), groupId);
        if (groupProfilesDBModel != null) {
            //UPDATE THE DB
            groupProfilesDBModel.delete();
        }

        saveUpdateGroup(addUpdateProfilesRequest);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private void saveUpdateGroup(AddUpdateProfilesRequest addUpdateProfilesRequest) {
        List<String> uidList = addUpdateProfilesRequest.getUidList();
        final String gid = addUpdateProfilesRequest.getGroupId();

        for (String uid : uidList) {
            final GroupProfileModel groupModel = GroupProfileModel.build(mAppContext.getDBSession(), gid, uid);

            mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
                @Override
                public Void perform(IDBSession dbSession) {
                    groupModel.save();
                    logGroupAuditEvent(gid);
                    return null;
                }
            });
        }
    }
}
