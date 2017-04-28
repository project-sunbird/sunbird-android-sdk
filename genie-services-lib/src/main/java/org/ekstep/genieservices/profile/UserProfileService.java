package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.profile.db.model.AnonymousUserModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;

import java.util.UUID;

/**
 * Any service related to profile will be called using ProfileService
 *
 * @author shriharsh
 */

public class UserProfileService extends BaseService {

    private static final String TAG = UserProfileService.class.getSimpleName();

    public UserProfileService(AppContext appContext) {
        super(appContext);
    }

    /**
     * Create a new user profile
     *
     * @param profile         - User profile data
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void createUserProfile(Profile profile, IResponseHandler<Profile> responseHandler) {
        GenieResponse<Profile> response = createProfile(profile);

        if (response != null) {
            if (response.getStatus()) {
                responseHandler.onSuccess(response);
            } else {
                responseHandler.onError(response);
            }
        }
    }

    private GenieResponse<Profile> createProfile(Profile profile) {
        if (profile != null && !profile.isValid()) {
            // TODO: 26/4/17 Need to create error event
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    profile.getErrors(), ""), "createProfile");

            return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG + " - createProfile");
        }

        return saveUserProfile(profile);
    }

    private GenieResponse<Profile> saveUserProfile(Profile profile) {
        // TODO: 24/4/17 Need to create Location Wrapper to get location
        String uid = UUID.randomUUID().toString();
        UserModel user = UserModel.buildUser(mAppContext, uid, profile);
        user.create();
        GenieResponse<Profile> successResponse = GenieResponse.getSuccessResponse("");
        successResponse.setResult(GsonUtil.fromJson(user.getProfile().toString(), Profile.class));

        return successResponse;
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
                setAnonymousUser();
            }

            //now delete the requested user id
            UserModel user = UserModel.findByUserId(mAppContext, uid);

            //if something goes wrong while deleting, then rollback the delete
            if (user != null) {
                user.delete(mAppContext);
            }

            responseHandler.onSuccess(GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE));
        } catch (Exception e) {
            Logger.e(mAppContext, "Error when deleting user profile", e.getMessage());
            rollBack(currentUserID);
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, e.toString(), ServiceConstants.ERROR_DELETING_A_USER, TAG));
        }

    }

    private void rollBack(String previousUserId) {
        UserModel previousCurrentUser = UserModel.findByUserId(mAppContext, previousUserId);
        setCurrentUser(previousCurrentUser);
    }

    private String getCurrentUserID() {
        UserSessionModel userSession = UserSessionModel.findUserSession(mAppContext, "");
        UserSession currentSession = userSession.find();
        if (currentSession != null && !currentSession.getUid().isEmpty())
            return currentSession.getUid();
        else
            return null;
    }

    private String setAnonymousUser() {
        AnonymousUserModel anonymousUser = AnonymousUserModel.findAnonymousUser(mAppContext);

        String uid = anonymousUser.getUid();
        if (uid == null || uid.isEmpty()) {
            uid = createAnonymousUser();
            if (uid.isEmpty()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.UNABLE_TO_CREATE_ANONYMOUS_USER, TAG).toString();
            }

        }

        setUserSession(uid);
        GenieResponse response = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        return response.toString();
    }

    private String setCurrentUser(UserModel user) {
        if (!user.exists()) {
            // TODO: 25/4/17 GEError Event has to be added here
            return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG).toString();
        }

        setUserSession(user.getUid());

        return GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE).toString();
    }

    private String createAnonymousUser() {
        //random user id generated
        String uid = UUID.randomUUID().toString();
        UserModel user = UserModel.buildUser(mAppContext, uid);

        try {
            user.create();
        } catch (DbException e) {
            return "";
        }

        return user.getUid();
    }

    private void setUserSession(String uid) {
        UserSessionModel userSession = UserSessionModel.buildUserSession(mAppContext, uid);
        userSession.save();
    }

    /**
     * Update user profile
     *
     * @param profile          - User profile data
     * @param responseHandler- the class which will receive the success or failure response
     *                         with the data.
     */
    public void updateUserProfile(Profile profile, IResponseHandler responseHandler) {
        GenieResponse genieResponse = update(profile);

        if (genieResponse != null) {
            if (genieResponse.getStatus()) {
                responseHandler.onSuccess(genieResponse);
            } else {
                responseHandler.onError(genieResponse);
            }
        }
    }

    private GenieResponse update(Profile profile) {
        if (profile != null) {
            String uid = profile.getUid();

            if (uid == null || uid.isEmpty()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_PROFILE, ServiceConstants.UNABLE_TO_FIND_PROFILE, TAG);
            }

            if (!profile.isValid()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG);

            }

            Profile newProfile = new Profile("", "", "");
            newProfile.setUid(profile.getUid());
            UserProfileModel newUserProfile = UserProfileModel.buildUserProfile(mAppContext, newProfile);


            if (!newUserProfile.getProfile().isValid()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_PROFILE, ServiceConstants.UNABLE_TO_FIND_PROFILE, TAG);
            }

            try {
                UserProfileModel userProfile = UserProfileModel.buildUserProfile(mAppContext, profile);
                userProfile.update(mAppContext);
            } catch (DbException e) {
                Logger.e(mAppContext, TAG, e.getMessage());
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, e.getMessage(), TAG);
            }
        }


        return GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    /**
     * set anonymous user
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void setAnonymousUser(IResponseHandler responseHandler) {
        AnonymousUserModel anonymousUser = AnonymousUserModel.findAnonymousUser(mAppContext);

        String uid = anonymousUser.getUid();
        if (uid == null || uid.isEmpty()) {
            uid = createAnonymousUser();
            if (uid.isEmpty()) {
                // TODO: 25/4/17 GEError Event has to be added here
                responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, DbConstants.ERROR, ServiceConstants.UNABLE_TO_CREATE_ANONYMOUS_USER, TAG));
            }

        }

        setUserSession(uid);
        GenieResponse successResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        responseHandler.onSuccess(successResponse);
    }

    /**
     * Get anonymous user data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void getAnonymousUser(IResponseHandler responseHandler) {
        AnonymousUserModel anonymousUser = AnonymousUserModel.findAnonymousUser(mAppContext);

        String uid = anonymousUser.getUid();
        Profile profile = new Profile("", "", "");
        profile.setUid(uid);

        GenieResponse successResponse = new GenieResponse<>();
        successResponse.setStatus(true);
        successResponse.setResult(GsonUtil.fromJson(profile.toString(), Profile.class));

        responseHandler.onSuccess(successResponse);
    }

    /**
     * set current user
     *
     * @param uid             - User id
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void setCurrentUser(String uid, IResponseHandler responseHandler) {
        UserModel user = UserModel.findByUserId(mAppContext, uid);

        if (!user.exists()) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG));
        }

        setUserSession(user.getUid());

        GenieResponse successResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        responseHandler.onSuccess(successResponse);
    }

    /**
     * Get current use data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void getCurrentUser(IResponseHandler responseHandler) {
        UserSessionModel userSession = UserSessionModel.buildUserSession(mAppContext, "");

        //get the current user session
        UserSession currentSession = userSession.find();

        if (currentSession != null && currentSession.getUid().isEmpty()) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NOT_EXISTS, ServiceConstants.NO_CURRENT_USER, TAG));
        }

        Profile profile = new Profile("", "", "");
        profile.setUid(currentSession.getUid());
        UserProfileModel userProfile = UserProfileModel.buildUserProfile(mAppContext, profile);

        GenieResponse successResponse = new GenieResponse<>();
        successResponse.setStatus(true);
        successResponse.setResult(GsonUtil.fromJson(userProfile.getProfile().toString(), Profile.class));

        responseHandler.onSuccess(successResponse);
    }

    /**
     * Unset current user data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void unsetCurrentUser(IResponseHandler responseHandler) {
        UserSessionModel userSession = UserSessionModel.buildUserSession(mAppContext, "");

        UserSession currentSession = userSession.find();

        if (currentSession != null && !currentSession.getUid().isEmpty()) {
            userSession.endCurrentSession();
        }

        GenieResponse successResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        responseHandler.onSuccess(successResponse);
    }

}
