package org.ekstep.genieservices.profile;

import com.google.gson.JsonSyntaxException;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.BuildConfigUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.profile.db.model.AnonymousUser;
import org.ekstep.genieservices.profile.db.model.User;

/**
 * Any service related to profile will be called using ProfileService
 *
 * @author shriharsh
 */

public class UserProfileService extends BaseService {

    private static final String TAG = UserProfileService.class.getSimpleName();
    private ProfileRequest mProfileRequest;
    private AppContext mAppContext;

    public UserProfileService(AppContext appContext) {
        super(appContext);
        mAppContext = appContext;
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

    private GenieResponse createProfile(ProfileRequest request) {
        Profile profile = null;

        try {
            profile = getProfileFromJson(request.profileJson());
        } catch (JsonSyntaxException ex) {
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    Collections.singletonList("invalid json"), ""), "createProfile");

            return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.INVALID_JSON, TAG);
        }

        if (profile != null && !profile.isValid()) {
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    profile.getErrors(), ""), "createProfile");

            return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.INVALID_JSON, TAG);

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
            return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.INVALID_JSON, TAG);

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

    /**
     * Delete user profile
     *
     * @param uid-            user id
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void deleteUserProfile(String uid, IResponseHandler responseHandler) {
        //get the current user id
        String currentUserID = getCurrentUserID();

        try {
            //match the current and requested user id if same then first set current user as anonymous
            if (uid.equals(currentUserID)) {
                setAnonymous(currentUserID);
            }

            //now delete the requested user id
            User user = User.findByUserId(mAppContext, uid);

            //if something goes wrong while deleting, then rollback the delete
            if (user != null) {
                user.delete(mAppContext, mAppContext.getDeviceInfo(), mAppContext.getAppPackage(), BuildConfigUtil.VERSION_NAME);
            }

            responseHandler.onSuccess(GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE));
        } catch (Exception e) {
            Logger.e(mAppContext, "Error when deleting user profile", e.getMessage());
            rollBack(currentUserID);
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, e.toString(), ServiceConstants.ERROR_DELETING_A_USER, TAG));
        }

    }

    private void rollBack(String previousUserId) {
        SetCurrentUserRequest request = new SetCurrentUserRequest(mAppContext.getAppPackage(), BuildConfigUtil.VERSION_NAME, previousUserId);
        User previousCurrentUser = User.findByUserId(mAppContext, request.uid());
        setCurrentUser(request, previousCurrentUser);
    }

    private String getCurrentUserID() {
        UserSession userSession = getUserSession();
        UserSession currentSession = userSession.getCurrentSession();
        if (currentSession.isValid())
            return currentSession.getUid();
        else
            return null;
    }

    private UserSession getUserSession() {
        return new UserSession(mAppContext, "");
    }

    private void setAnonymous(String currentUserID) {
        if (currentUserID != null) {
            SetAnonymousUserRequest request = new SetAnonymousUserRequest(mAppContext.getAppPackage(), BuildConfigUtil.VERSION_NAME);
            setAnonymousUser(request);
        }
    }

    public String setAnonymousUser(SetAnonymousUserRequest request) {
        String query = "select u.uid from users u left join profiles p on p.uid=u.uid where p.uid is null and u.uid is not null";

        AnonymousUser anonymousUser = AnonymousUser.findAnonymousUser(mAppContext, query);

        String uid = anonymousUser.getUid();
        if (uid == null || uid.isEmpty()) {
            uid = createAnonymousUser(request.gameID(), request.gameVersion());
            if (uid.isEmpty()) {
//                logAndSendResponse(request.gameID(), request.gameVersion(),
//                        new Response("failed", "DB_ERROR",
//                                Collections.singletonList("unable to create anonymous user"), ""), "setAnonymousUser");

                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.UNABLE_TO_CREATE_ANONYMOUS_USER, TAG).toString();
            }

        }

        setUserSession(request.gameID(), request.gameVersion(), uid);
        GenieResponse response = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        return response.toString();
    }

    private String setCurrentUser(SetCurrentUserRequest request, User user) {
        if (!user.exists()) {
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "INVALID_USER",
//                    Collections.singletonList("There is no user with specified id exists"), ""), "setCurrentUser");
            // TODO: 25/4/17 GEError Event has to be added here
            return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG).toString();
        }

        setUserSession(request.gameID(), request.gameVersion(), user.getUid());

        return GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE).toString();
    }

    private String createAnonymousUser(String gameID, String gameVersion) {
        // TODO: 25/4/17 Have to get location here
        String location = "";
        User user = User.buildUser(mAppContext);

        try {
            user.create(gameID, gameVersion, location, mAppContext.getDeviceInfo());
        } catch (DbException e) {
            return "";
        }

        return user.getUid();
    }

    protected void setUserSession(String gameID, String gameVersion, String uid) {
        // TODO: 25/4/17 Have to get location here
        String location = "";
        UserSession userSession = new UserSession(mAppContext, uid);
        userSession.save(gameID, gameVersion, location, mAppContext.getDeviceInfo());
    }


}
