package org.ekstep.genieservices.profile;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.AcceptTermsAndConditionsRequest;
import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.FileUploadResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UpdateUserInfoRequest;
import org.ekstep.genieservices.commons.bean.UploadFileRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.bean.UserProfileSkill;
import org.ekstep.genieservices.commons.bean.UserProfileSkillsRequest;
import org.ekstep.genieservices.commons.bean.UserSearchCriteria;
import org.ekstep.genieservices.commons.bean.UserSearchResult;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.List;
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

    private <T> GenieResponse<T> isValidAuthSession(String methodName, Map<String, Object> params) {
        if (authSession == null || authSession.getSessionData() == null) {
            GenieResponse<T> response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.AUTH_SESSION,
                    ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.USER_NOT_SIGN_IN);
            return response;
        }

        return null;
    }

    @Override
    public GenieResponse<UserProfile> getUserProfileDetails(UserProfileDetailsRequest userProfileDetailsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(userProfileDetailsRequest));
        params.put("logLevel", "2");
        String methodName = "getUserProfileDetails@UserProfileServiceImpl";

        GenieResponse<UserProfile> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        String fields = "";
        if (!CollectionUtil.isNullOrEmpty(userProfileDetailsRequest.getRequiredFields())) {
            fields = "fields=" + StringUtil.join(",", userProfileDetailsRequest.getRequiredFields());
        }

        String key = USER_PROFILE_DETAILS_KEY_PREFIX + userProfileDetailsRequest.getUserId();
        NoSqlModel userProfileInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (userProfileInDB == null) {
            GenieResponse userProfileDetailsAPIResponse = UserProfileHandler.fetchUserProfileDetailsFromServer(mAppContext,
                    authSession.getSessionData(), userProfileDetailsRequest.getUserId(), fields);
            if (userProfileDetailsAPIResponse.getStatus()) {
                String body = userProfileDetailsAPIResponse.getResult().toString();
                userProfileInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                userProfileInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(userProfileDetailsAPIResponse.getError(), userProfileDetailsAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
                return response;
            }
        } else if (userProfileDetailsRequest.isReturnRefreshedUserProfileDetails()) {
            GenieResponse userProfileDetailsAPIResponse = UserProfileHandler.fetchUserProfileDetailsFromServer(mAppContext,
                    authSession.getSessionData(), userProfileDetailsRequest.getUserId(), fields);

            if (userProfileDetailsAPIResponse.getStatus()) {
                String jsonResponse = userProfileDetailsAPIResponse.getResult().toString();
                if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                    userProfileInDB.setValue(jsonResponse);
                    userProfileInDB.update();
                }
            }
        } else if (userProfileDetailsRequest.isRefreshUserProfileDetails()) {
            UserProfileHandler.refreshUserProfileDetailsFromServer(mAppContext, authSession.getSessionData(),
                    userProfileDetailsRequest.getUserId(), fields, userProfileInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(userProfileInDB.getValue(), LinkedTreeMap.class);
        LinkedTreeMap resultMap = (LinkedTreeMap) map.get("result");
        if (resultMap != null && resultMap.containsKey("response")) {
            String responseString = GsonUtil.toJson(resultMap.get("response"));
            UserProfile userProfile = new UserProfile(responseString);
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(userProfile);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_DATA_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }

        return response;
    }

    @Override
    public GenieResponse<TenantInfo> getTenantInfo(TenantInfoRequest tenantInfoRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(tenantInfoRequest));
        params.put("logLevel", "2");
        String methodName = "getTenantInfo@UserProfileServiceImpl";

        GenieResponse<TenantInfo> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        String key = TENANT_INFO_KEY_PREFIX + tenantInfoRequest.getSlug();
        NoSqlModel tenantInfoInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (tenantInfoInDB == null) {
            GenieResponse tenantInfoAPIResponse = UserProfileHandler.fetchTenantInfoFromServer(mAppContext,
                    authSession.getSessionData(), tenantInfoRequest.getSlug());
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
            UserProfileHandler.refreshTenantInfoFromServer(mAppContext, authSession.getSessionData(),
                    tenantInfoRequest.getSlug(), tenantInfoInDB);
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
    public GenieResponse<UserSearchResult> searchUser(UserSearchCriteria userSearchCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(userSearchCriteria));
        params.put("logLevel", "2");
        String methodName = "searchUser@UserProfileServiceImpl";

        GenieResponse<UserSearchResult> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse searchUserAPIResponse = UserProfileHandler.searchUser(mAppContext, authSession.getSessionData(), userSearchCriteria);

        if (searchUserAPIResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(searchUserAPIResponse.getResult().toString(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(((Map) map.get("result")).get("response"));
            UserSearchResult userSearchResult = new UserSearchResult(result);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(userSearchResult);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(searchUserAPIResponse.getError(), searchUserAPIResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, searchUserAPIResponse.getMessage());
        }

        return response;
    }

    public GenieResponse<UserProfileSkill> getSkills(UserProfileSkillsRequest profileSkillsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(profileSkillsRequest));
        params.put("logLevel", "2");
        String methodName = "getSkills@UserProfileServiceImpl";

        GenieResponse<UserProfileSkill> response;

        String key = USER_PROFILE_SKILLS;
        NoSqlModel profileSkillsInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (profileSkillsInDB == null) {
            GenieResponse skillsAPIResponse = UserProfileHandler.fetchProfileSkillsFromServer(mAppContext, authSession.getSessionData());
            if (skillsAPIResponse.getStatus()) {
                String body = skillsAPIResponse.getResult().toString();
                profileSkillsInDB = NoSqlModel.build(mAppContext.getDBSession(), key, body);
                profileSkillsInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(skillsAPIResponse.getError(), skillsAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, skillsAPIResponse.getMessage());
                return response;
            }
        } else if (profileSkillsRequest.isRefreshProfileSkills()) {
            UserProfileHandler.refreshProfileSkillsFromServer(mAppContext, authSession.getSessionData(), profileSkillsInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(profileSkillsInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        UserProfileSkill profileSkills = GsonUtil.fromJson(result, UserProfileSkill.class);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profileSkills);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> endorseOrAddSkill(EndorseOrAddSkillRequest endorseOrAddSkillRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(endorseOrAddSkillRequest));
        params.put("logLevel", "2");
        String methodName = "endorseOrAddSkill@UserProfileServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse endorseOrAddSkillAPIResponse = UserProfileHandler.endorseOrAddSkillsInServer(mAppContext,
                authSession.getSessionData(), endorseOrAddSkillRequest);
        if (endorseOrAddSkillAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(endorseOrAddSkillAPIResponse.getError(),
                    endorseOrAddSkillAPIResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }

        return response;
    }

    @Override
    public GenieResponse<Void> setProfileVisibility(ProfileVisibilityRequest profileVisibilityRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(profileVisibilityRequest));
        params.put("logLevel", "2");
        String methodName = "setProfileVisibility@UserProfileServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse profileVisibilityAPIResponse = UserProfileHandler.setProfileVisibilityDetailsInServer(mAppContext,
                authSession.getSessionData(), profileVisibilityRequest);

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
    public GenieResponse<FileUploadResult> uploadFile(UploadFileRequest uploadFileRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(uploadFileRequest));
        params.put("logLevel", "2");
        String methodName = "updateUserInfo@UserProfileServiceImpl";

        GenieResponse<FileUploadResult> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse fileUploadResponse = UserProfileHandler.uploadFile(mAppContext, authSession.getSessionData(), uploadFileRequest);

        if (fileUploadResponse.getStatus()) {
            LinkedTreeMap map = GsonUtil.fromJson(fileUploadResponse.getResult().toString(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            FileUploadResult fileUploadResult = GsonUtil.fromJson(result, FileUploadResult.class);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(fileUploadResult);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(fileUploadResponse.getError(), fileUploadResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, fileUploadResponse.getMessage());
        }

        return response;
    }

    @Override
    public GenieResponse<Void> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(updateUserInfoRequest));
        params.put("logLevel", "2");
        String methodName = "updateUserInfo@UserProfileServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse updateUserInfoAPIResponse = UserProfileHandler.updateUserInfoInServer(mAppContext,
                authSession.getSessionData(), updateUserInfoRequest);

        if (updateUserInfoAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = updateUserInfoAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(updateUserInfoAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }

    @Override
    public GenieResponse<Void> acceptTermsAndConditions(AcceptTermsAndConditionsRequest acceptTermsAndConditionsRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(acceptTermsAndConditionsRequest));
        params.put("logLevel", "2");
        String methodName = "acceptTermsAndConditions@UserProfileServiceImpl";

        GenieResponse<Void> response = isValidAuthSession(methodName, params);
        if (response != null) {
            return response;
        }

        GenieResponse acceptTermsAndConditionsAPIResponse = UserProfileHandler.acceptTermsAndConditions(mAppContext,
                authSession.getSessionData(), acceptTermsAndConditionsRequest);

        if (acceptTermsAndConditionsAPIResponse.getStatus()) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            List<String> errorMessages = acceptTermsAndConditionsAPIResponse.getErrorMessages();
            String errorMessage = null;
            if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                errorMessage = errorMessages.get(0);
            }
            response = GenieResponseBuilder.getErrorResponse(acceptTermsAndConditionsAPIResponse.getError(), errorMessage, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
        }

        return response;
    }

}
