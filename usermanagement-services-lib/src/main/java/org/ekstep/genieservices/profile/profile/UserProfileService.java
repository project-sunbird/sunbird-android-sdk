package org.ekstep.genieservices.profile.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.profile.db.model.AnonymousUserModel;
import org.ekstep.genieservices.profile.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.profile.db.model.UserSessionModel;

import java.util.Date;
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
        GenieResponse<Profile> response = null;
        if (profile != null && !profile.isValid()) {
            // TODO: 26/4/17 Need to create error event
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    profile.getErrors(), ""), "createProfile");
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG + " - createProfile", Profile.class);
        } else {
            response = saveUserProfile(profile, mAppContext.getDBSession());
        }
        if (response != null) {
            if (response.getStatus()) {
                responseHandler.onSuccess(response);
            } else {
                responseHandler.onError(response);
            }
        }
    }

    private GenieResponse<Profile> saveUserProfile(final Profile profile, IDBSession dbSession) {
        // TODO: 24/4/17 Need to create Location Wrapper to get location
        String uid = UUID.randomUUID().toString();
        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(new Date());
        }
        final UserModel userModel = UserModel.build(dbSession, uid);

        profile.setUid(uid);
        final UserProfileModel profileModel = UserProfileModel.buildUserProfile(dbSession, profile);
        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                userModel.save();
                // TODO: 24/4/17 Should add telemetry event after creating a new user

                profileModel.save();
                // TODO: 24/4/17 Should add telemetry event after creating ProfileDTO

                return null;
            }
        });
        GenieResponse<Profile> successResponse = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        successResponse.setResult(GsonUtil.fromJson(profileModel.getProfile().toString(), Profile.class));

        return successResponse;
    }

    /**
     * Delete user profile
     *
     * @param uid-            user id
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void deleteUserProfile(String uid, IResponseHandler<String> responseHandler) {
        //get the current user id
        UserSessionModel userSession = UserSessionModel.findUserSession(mAppContext);
        if (userSession != null) {
            if (userSession.getUserSessionBean().getUid().equals(uid)) {
                setAnonymousUser();
            }
        }
        final ContentAccessesModel accessesModel = ContentAccessesModel.findByUid(mAppContext.getDBSession(), uid);
        final UserProfileModel userProfileModel = UserProfileModel.findUserProfile(mAppContext.getDBSession(), uid);
        final UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);
        mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                accessesModel.delete();
                userProfileModel.delete();
                // TODO: 24/4/17 Should add telemetry event after deleting a profile
                userModel.delete();
                // TODO: 24/4/17 Should add telemetry event after deleting user

                return null;
            }
        });
        GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse("", String.class);
        response.setResult(uid);
        responseHandler.onSuccess(response);

    }

     private GenieResponse<String> setAnonymousUser() {
        AnonymousUserModel anonymousUserModel = AnonymousUserModel.findAnonymousUser(mAppContext.getDBSession());
        String uid = null;
        if (anonymousUserModel == null) {
            uid = createAnonymousUser();
        } else {
            uid = anonymousUserModel.getUid();
        }
        setUserSession(uid);
        GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, String.class);
        response.setResult(uid);
        return response;
    }

    private String createAnonymousUser() {
        //random user id generated
        String uid = UUID.randomUUID().toString();
        UserModel user = UserModel.build(mAppContext.getDBSession(), uid);
        user.save();
        return user.getUid();
    }

    private void setUserSession(String uid) {
        // TODO Telemetry for GE_SESSION_END for current user and GE_SESSION_START for new user
        // TODO Add check to not start a new session if the same uid is being set again
        UserSessionModel userSessionModel = UserSessionModel.buildUserSession(mAppContext, uid);
        userSessionModel.startSession();
    }

    /**
     * Update user profile
     *
     * @param profile          - User profile data
     * @param responseHandler- the class which will receive the success or failure response
     *                         with the data.
     */
    public void updateUserProfile(Profile profile, IResponseHandler<Profile> responseHandler) {
        GenieResponse genieResponse = update(profile);

        if (genieResponse != null) {
            if (genieResponse.getStatus()) {
                responseHandler.onSuccess(genieResponse);
            } else {
                responseHandler.onError(genieResponse);
            }
        }
    }

    private GenieResponse<Profile> update(Profile profile) {
        if (profile != null) {
            String uid = profile.getUid();

            if (uid == null || uid.isEmpty()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponseBuilder.getErrorResponse(ServiceConstants.INVALID_PROFILE, ServiceConstants.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            }

            if (!profile.isValid()) {
                // TODO: 25/4/17 GEError Event has to be added here
                return GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG, Profile.class);

            }
            // TODO: 26/4/17 GEUpdateEvent has to be added to the telemetry
            UserProfileModel userProfileModel = UserProfileModel.buildUserProfile(mAppContext.getDBSession(), profile);
            userProfileModel.update();
        }

        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
        response.setResult(profile);
        return response;
    }

    /**
     * set anonymous user
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void setAnonymousUser(IResponseHandler<String> responseHandler) {
        responseHandler.onSuccess(setAnonymousUser());
    }

    /**
     * Get anonymous user data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void getAnonymousUser(IResponseHandler<Profile> responseHandler) {
        AnonymousUserModel anonymousUserModel = AnonymousUserModel.findAnonymousUser(mAppContext.getDBSession());

        String uid = null;
        if (anonymousUserModel == null) {
            uid = createAnonymousUser();
        } else {
            uid = anonymousUserModel.getUid();
        }
        Profile profile = new Profile(uid);

        GenieResponse successResponse = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        successResponse.setResult(profile);
        responseHandler.onSuccess(successResponse);
    }

    /**
     * set current user
     *
     * @param uid             - User id
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void setCurrentUser(String uid, IResponseHandler<Void> responseHandler) {
        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);

        if (userModel == null) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponseBuilder.getErrorResponse(ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG, Void.class));
        }

        setUserSession(userModel.getUid());

        GenieResponse<Void> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
        responseHandler.onSuccess(successResponse);
    }

    /**
     * Get current use data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void getCurrentUser(IResponseHandler<Profile> responseHandler) {
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        if (userSessionModel == null) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponseBuilder.getErrorResponse(ServiceConstants.NOT_EXISTS, ServiceConstants.NO_CURRENT_USER, TAG, Profile.class));
        }

        UserProfileModel userProfileModel = UserProfileModel.findUserProfile(mAppContext.getDBSession(), userSessionModel.getUserSessionBean().getUid());

        GenieResponse<Profile> successResponse = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        successResponse.setResult(userProfileModel.getProfile());
        responseHandler.onSuccess(successResponse);
    }

    /**
     * Unset current user data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void unsetCurrentUser(IResponseHandler<Void> responseHandler) {
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        if (userSessionModel != null) {
            // TODO: 26/4/17 Add GESessionEnd event
//        GESessionEnd geSessionEnd = new GESessionEnd(gameID, gameVersion, currentSession, deviceInfo.getDeviceID());
//        Event event = new Event(geSessionEnd.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionEnd.toString());
//        event.save(dbOperator);
            userSessionModel.endSession();
        }
        GenieResponse<Void> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
        responseHandler.onSuccess(successResponse);
    }

}
