package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
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

        String fields = "";
        if (!CollectionUtil.isNullOrEmpty(userProfileDetailsRequest.getRequiredFields())) {
            fields = "fields?" + StringUtil.join(",", userProfileDetailsRequest.getRequiredFields());
        }

        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(mAppContext, userProfileDetailsRequest.getUserId(), fields);
        GenieResponse genieResponse = userProfileDetailsAPI.get();

        GenieResponse<UserProfile> response;
        if (genieResponse.getStatus()) {
            String jsonStr = genieResponse.getResult().toString();
            UserProfile userProfile = new UserProfile(jsonStr);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(userProfile);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, genieResponse.getMessage());
        }
        return response;
    }
}
