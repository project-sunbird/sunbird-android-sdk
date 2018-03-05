package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.network.UserProfileDetailsAPI;
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

    public UserProfileServiceImpl(AppContext appContext) {
        super(appContext);
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

        String key = userProfileDetailsRequest.getUserId();
        NoSqlModel userProfileInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (userProfileInDB == null) {
            GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(userProfileDetailsRequest.getUserId(), fields);
            if (userProfileDetailsAPIResponse.getStatus()) {
                String jsonResponse = userProfileDetailsAPIResponse.getResult().toString();
                userProfileInDB = NoSqlModel.build(mAppContext.getDBSession(), key, jsonResponse);
                userProfileInDB.save();
            } else {
                response = GenieResponseBuilder.getErrorResponse(userProfileDetailsAPIResponse.getError(), userProfileDetailsAPIResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, userProfileDetailsAPIResponse.getMessage());
                return response;
            }

        } else if (userProfileDetailsRequest.isRefreshUserProfileDetails()) {
            refreshUserProfileDetailsFromServer(userProfileDetailsRequest.getUserId(), fields, userProfileInDB);
        }

        UserProfile userProfile = new UserProfile(userProfileInDB.getValue());
        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(userProfile);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private GenieResponse fetchUserProfileDetailsFromServer(String userId, String fields) {
        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(mAppContext, userId, fields);
        return userProfileDetailsAPI.get();
    }

    private void refreshUserProfileDetailsFromServer(final String userId, final String fields, final NoSqlModel userProfileInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(userId, fields);
                if (userProfileDetailsAPIResponse.getStatus()) {
                    String jsonResponse = userProfileDetailsAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        userProfileInDB.setValue(jsonResponse);
                        userProfileInDB.update();
                    }
                }
            }
        }).start();
    }
}
