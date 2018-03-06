package org.ekstep.genieservices.profile;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
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

        String fields = "";
        if (!CollectionUtil.isNullOrEmpty(userProfileDetailsRequest.getRequiredFields())) {
            fields = "fields?" + StringUtil.join(",", userProfileDetailsRequest.getRequiredFields());
        }

        String key = USER_PROFILE_DETAILS_KEY_PREFIX + userProfileDetailsRequest.getUserId();
        NoSqlModel userProfileInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (userProfileInDB == null) {
            GenieResponse userProfileDetailsAPIResponse = UserProfileHandler.fetchUserProfileDetailsFromServer(mAppContext, userProfileDetailsRequest.getUserId(), fields);
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
            UserProfileHandler.refreshUserProfileDetailsFromServer(mAppContext, userProfileDetailsRequest.getUserId(), fields, userProfileInDB);
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

        String key = TENANT_INFO_KEY_PREFIX + tenantInfoRequest.getSlug();
        NoSqlModel tenantInfoInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (tenantInfoInDB == null) {
            GenieResponse tenantInfoAPIResponse = UserProfileHandler.fetchTenantInfoFromServer(mAppContext, tenantInfoRequest.getSlug());
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
            UserProfileHandler.refreshTenantInfoFromServer(mAppContext, tenantInfoRequest.getSlug(), tenantInfoInDB);
        }

        LinkedTreeMap map = GsonUtil.fromJson(tenantInfoInDB.getValue(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        TenantInfo tenantInfo = GsonUtil.fromJson(result, TenantInfo.class);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(tenantInfo);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }
}
