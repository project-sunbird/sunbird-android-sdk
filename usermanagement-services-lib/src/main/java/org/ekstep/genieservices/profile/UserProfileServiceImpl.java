package org.ekstep.genieservices.profile;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.SearchUserRequest;
import org.ekstep.genieservices.commons.bean.SearchUserResult;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.bean.UserProfileSkills;
import org.ekstep.genieservices.commons.bean.UserProfileSkillsRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 1/3/18.
 *
 * @author anil
 */
public class UserProfileServiceImpl extends BaseService implements IUserProfileService {

    private static final String TAG = UserProfileServiceImpl.class.getSimpleName();

    private static final String USER_PROFILE_DETAILS_KEY_PREFIX = "userProfileDetails";
    private static final String TENANT_INFO_KEY_PREFIX = "tenantInfo";
    private static final String USER_PROFILE_SKILLS = "userProfileSkills";

    private IAuthSession<Session> authSession;

    public UserProfileServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);
        this.authSession = authSession;
    }

    @Override
    public GenieResponse<UserProfile> getUserProfileDetails(UserProfileDetailsRequest userProfileDetailsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(userProfileDetailsRequest));
        String methodName = "getUserProfileDetails@UserProfileServiceImpl";

        GenieResponse<UserProfile> response;

        if (authSession == null || authSession.getSessionData() == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        String fields = "";
        if (!CollectionUtil.isNullOrEmpty(userProfileDetailsRequest.getRequiredFields())) {
            fields = "fields?" + StringUtil.join(",", userProfileDetailsRequest.getRequiredFields());
        }

        String key = USER_PROFILE_DETAILS_KEY_PREFIX + userProfileDetailsRequest.getUserId();
        NoSqlModel userProfileInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (userProfileInDB == null) {
            GenieResponse userProfileDetailsAPIResponse = UserProfileHandler.fetchUserProfileDetailsFromServer(mAppContext, authSession.getSessionData(), userProfileDetailsRequest.getUserId(), fields);
            if (userProfileDetailsAPIResponse.getStatus()) {
                String body = userProfileDetailsAPIResponse.getResult().toString();
                userProfileInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                userProfileInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(userProfileDetailsAPIResponse.getError(), userProfileDetailsAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, userProfileDetailsAPIResponse.getMessage());
                return response;
            }
        } else if (userProfileDetailsRequest.isRefreshUserProfileDetails()) {
            UserProfileHandler.refreshUserProfileDetailsFromServer(mAppContext, authSession.getSessionData(), userProfileDetailsRequest.getUserId(), fields, userProfileInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(userProfileInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        UserProfile userProfile = new UserProfile(result);
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(userProfile);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<TenantInfo> getTenantInfo(TenantInfoRequest tenantInfoRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(tenantInfoRequest));
        String methodName = "getTenantInfo@UserProfileServiceImpl";

        GenieResponse<TenantInfo> response;

        if (authSession == null || authSession.getSessionData() == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        String key = TENANT_INFO_KEY_PREFIX + tenantInfoRequest.getSlug();
        NoSqlModel tenantInfoInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (tenantInfoInDB == null) {
            GenieResponse tenantInfoAPIResponse = UserProfileHandler.fetchTenantInfoFromServer(mAppContext, authSession.getSessionData(), tenantInfoRequest.getSlug());
            if (tenantInfoAPIResponse.getStatus()) {
                String body = tenantInfoAPIResponse.getResult().toString();
                tenantInfoInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                tenantInfoInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(tenantInfoAPIResponse.getError(), tenantInfoAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, tenantInfoAPIResponse.getMessage());
                return response;
            }
        } else if (tenantInfoRequest.isRefreshTenantInfo()) {
            UserProfileHandler.refreshTenantInfoFromServer(mAppContext, authSession.getSessionData(), tenantInfoRequest.getSlug(), tenantInfoInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(tenantInfoInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        TenantInfo tenantInfo = GsonUtil.fromJson(result, TenantInfo.class);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(tenantInfo);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> setProfileVisibility(ProfileVisibilityRequest profileVisibilityRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(profileVisibilityRequest));
        String methodName = "setProfileVisibility@UserProfileServiceImpl";

        GenieResponse<Void> response;

        if (authSession == null || authSession.getSessionData() == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        GenieResponse profileVisibilityAPIResponse = UserProfileHandler.setProfileVisibilityDetailsInServer(mAppContext, authSession.getSessionData(), profileVisibilityRequest);

        if (profileVisibilityAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(profileVisibilityAPIResponse.getError(), profileVisibilityAPIResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, profileVisibilityAPIResponse.getMessage());
        }
        return response;
    }

    @Override
    public GenieResponse<SearchUserResult> searchUser(SearchUserRequest searchUserRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(searchUserRequest));
        String methodName = "searchUser@UserProfileServiceImpl";

        GenieResponse<SearchUserResult> response;

        if (authSession == null || authSession.getSessionData() == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        GenieResponse searchUserAPIResponse = UserProfileHandler.searchUser(mAppContext, authSession.getSessionData(), searchUserRequest);

        if (searchUserAPIResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(searchUserAPIResponse.getResult().toString(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            SearchUserResult searchUserResult = GsonUtil.fromJson(result, SearchUserResult.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(searchUserResult);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(searchUserAPIResponse.getError(), searchUserAPIResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, searchUserAPIResponse.getMessage());
        }

        return response;
    }

    public GenieResponse<UserProfileSkills> getUserProfileSkills(UserProfileSkillsRequest profileSkillsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(profileSkillsRequest));
        String methodName = "getUserProfileSkills@UserProfileServiceImpl";

        GenieResponse<UserProfileSkills> response;

        String key = USER_PROFILE_SKILLS;
        NoSqlModel profileSkillsInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (profileSkillsInDB == null) {
            GenieResponse profileSkillsAPIResponse = UserProfileHandler.fetchProfileSkillsFromServer(mAppContext);
            if (profileSkillsAPIResponse.getStatus()) {
                String body = profileSkillsAPIResponse.getResult().toString();
                profileSkillsInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                profileSkillsInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(profileSkillsAPIResponse.getError(), profileSkillsAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, profileSkillsAPIResponse.getMessage());
                return response;
            }
        } else if (profileSkillsRequest.isRefreshProfileSkills()) {
            UserProfileHandler.refreshProfileSkillsFromServer(mAppContext, profileSkillsInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(profileSkillsInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        UserProfileSkills profileSkills = GsonUtil.fromJson(result, UserProfileSkills.class);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profileSkills);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> endorseOrAddSkill(EndorseOrAddSkillRequest endorseOrAddSkillRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(endorseOrAddSkillRequest));
        String methodName = "endorseOrAddSkill@UserProfileServiceImpl";

        GenieResponse<Void> response = UserProfileHandler.
                endorseOrAddSkillsFromServer(mAppContext, endorseOrAddSkillRequest.getUserId(), endorseOrAddSkillRequest.getSkills());

        if (response.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(response.getError(),
                    response.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

}
