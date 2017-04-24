package org.ekstep.genieservices.profile;

import com.google.gson.JsonSyntaxException;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.profile.db.model.User;

import java.util.Collections;

/**
 * Any service related to profile will be called using ProfileService
 *
 * @author shriharsh
 */

public class ProfileService extends BaseService {

    private ProfileRequest mProfileRequest;
    private AppContext mAppContext;

    public ProfileService(AppContext appContext) {
        super(appContext);
    }

    /**
     * Create a new user profile
     *
     * @param profile         - User profile data
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void createUserProfile(Profile profile, IResponseHandler responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        //create profile request
        mProfileRequest = new ProfileRequest(profile.toString());

        createProfile(mProfileRequest);

    }

    public GenieResponse createProfile(ProfileRequest request) {
        Profile profile = null;

        try {
            profile = getProfileFromJson(request.profileJson());
        } catch (JsonSyntaxException ex) {
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    Collections.singletonList("invalid json"), ""), "createProfile");

            GenieResponse<String> errorResponse = new GenieResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setError(DbConstants.ERROR);
            errorResponse.setErrorMessages(Collections.singletonList("invalid json"));

            return errorResponse;
        }

        if (profile != null && !profile.isValid()) {
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    profile.getErrors(), ""), "createProfile");

            GenieResponse<String> errorResponse = new GenieResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setError(DbConstants.ERROR);
            errorResponse.setErrorMessages(Collections.singletonList("invalid json"));

            return errorResponse;
        }

        GenieResponse successResponse = saveUserAndProfile(request.appID(), request.appVersion(), profile);

        return successResponse;
    }

    private GenieResponse saveUserAndProfile(String gameID, String gameVersion, Profile profile) {
        // TODO: 24/4/17 Need to create Location Wrapper to get location
        String location = "";
        User user = User.buildUser(mAppContext, profile);

        try {
            user.create(gameID, gameVersion, location, mAppContext.getDeviceInfo());
        } catch (DbException e) {
            GenieResponse<String> errorResponse = new GenieResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setError(DbConstants.ERROR);
            errorResponse.setErrorMessages(Collections.singletonList("invalid json"));

            return errorResponse;
        }

        GenieResponse successResponse = new GenieResponse<>();
        successResponse.setStatus(true);
        // TODO: 24/4/17 Shouold we be sending directly object or convert to string and return
        successResponse.setResult(GsonUtil.fromJson(user.getProfile().toString(), Profile.class));

        return successResponse;
    }

    private Profile getProfileFromJson(String profileJson) {
        Profile profile = GsonUtil.fromJson(profileJson, Profile.class);

        if (profile == null) {
            throw new JsonSyntaxException("invalid json");
        }

        return profile;
    }

}
