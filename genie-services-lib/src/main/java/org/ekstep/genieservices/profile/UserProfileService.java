package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.db.model.AnonymousUserModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;

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
            response = GenieResponse.getErrorResponse(mAppContext, ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG + " - createProfile");
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
        GenieResponse<Profile> successResponse = GenieResponse.getSuccessResponse("");
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
    public void deleteUserProfile(String uid, IResponseHandler responseHandler) {
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

    }

     private GenieResponse setAnonymousUser() {
        AnonymousUserModel anonymousUserModel = AnonymousUserModel.findAnonymousUser(mAppContext.getDBSession());
        String uid = null;
        if (anonymousUserModel == null) {
            uid = createAnonymousUser();
        } else {
            uid = anonymousUserModel.getUid();
        }
        setUserSession(uid);
        return GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
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
            // TODO: 26/4/17 GEUpdateEvent has to be added to the telemetry
            UserProfileModel userProfileModel = UserProfileModel.buildUserProfile(mAppContext.getDBSession(), profile);
            userProfileModel.update();
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

        GenieResponse successResponse = new GenieResponse<Profile>();
        successResponse.setStatus(true);
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
    public void setCurrentUser(String uid, IResponseHandler responseHandler) {
        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);

        if (userModel == null) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG));
        }

        setUserSession(userModel.getUid());

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
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        if (userSessionModel == null) {
            // TODO: 25/4/17 GEError Event has to be added here
            responseHandler.onError(GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NOT_EXISTS, ServiceConstants.NO_CURRENT_USER, TAG));
        }

        UserProfileModel userProfileModel = UserProfileModel.findUserProfile(mAppContext.getDBSession(), userSessionModel.getUserSessionBean().getUid());

        GenieResponse successResponse = GenieResponse.getSuccessResponse("");
        successResponse.setResult(userProfileModel.getProfile());
        responseHandler.onSuccess(successResponse);
    }

    /**
     * Unset current user data
     *
     * @param responseHandler - the class which will receive the success or failure response
     *                        with the data.
     */
    public void unsetCurrentUser(IResponseHandler responseHandler) {
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        if (userSessionModel != null) {
            // TODO: 26/4/17 Add GESessionEnd event
//        GESessionEnd geSessionEnd = new GESessionEnd(gameID, gameVersion, currentSession, deviceInfo.getDeviceID());
//        Event event = new Event(geSessionEnd.getEID(), TelemetryTagCache.activeTags(dbOperator, context())).withEvent(geSessionEnd.toString());
//        event.save(dbOperator);
            userSessionModel.endSession();
        }
        GenieResponse successResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        responseHandler.onSuccess(successResponse);
    }

}
