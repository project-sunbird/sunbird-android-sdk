package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateProfile;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateUser;
import org.ekstep.genieservices.commons.bean.telemetry.GEDeleteProfile;
import org.ekstep.genieservices.commons.bean.telemetry.GEError;
import org.ekstep.genieservices.commons.bean.telemetry.GESessionEnd;
import org.ekstep.genieservices.commons.bean.telemetry.GESessionStart;
import org.ekstep.genieservices.commons.bean.telemetry.GEUpdateProfile;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Any service related to profile will be called using ProfileService
 *
 * @author shriharsh
 */

public class UserServiceImpl extends BaseService implements IUserService {

    private static final String TAG = UserServiceImpl.class.getSimpleName();

    private GameData mGameData = null;

    public UserServiceImpl(AppContext appContext) {
        super(appContext);
        mGameData = new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName());
    }

    /**
     * Create a new user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> createUserProfile(Profile profile) {
        HashMap params = new HashMap();
        params.put("logLevel", CommonConstants.LOG_LEVEL);

        GenieResponse<Profile> response;
        if (profile == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_PROFILE, "invalid profile", "createUserProfile@UserServiceImpl", Profile.class);
            logGEError(response, "createUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, "createUserProfile@UserServiceImpl", params, "Unable to create profile");
            return response;
        } else if (profile != null && !profile.isValid()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, profile.getErrors().toString(), "createUserProfile@UserServiceImpl", Profile.class);
            logGEError(response, "createUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, "createUserProfile@UserServiceImpl", params, "Unable to create profile");
            return response;
        } else {
            response = saveUserProfile(profile, mAppContext.getDBSession());
        }

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "createUserProfile@UserServiceImpl", params);

        return response;
    }

    private void logGEError(GenieResponse response, String id) {
        GEError geError = new GEError(mGameData, response.getError(), id, "", response.getErrorMessages().toString());
        TelemetryLogger.log(geError);
    }


    private GenieResponse<Profile> saveUserProfile(final Profile profile, IDBSession dbSession) {
        String uid = UUID.randomUUID().toString();
        profile.setUid(uid);
        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(DateUtil.now());
        }
        final UserModel userModel = UserModel.build(dbSession, uid);

        final GECreateUser geCreateUser = new GECreateUser(mGameData, uid, mAppContext.getLocationInfo().getLocation());
        final UserProfileModel profileModel = UserProfileModel.buildUserProfile(dbSession, profile);
        final GECreateProfile geCreateProfile = new GECreateProfile(mGameData, profile, mAppContext.getLocationInfo().getLocation());
        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                userModel.save();
                TelemetryLogger.log(geCreateUser);
                profileModel.save();
                TelemetryLogger.log(geCreateProfile);
                return null;
            }
        });
        GenieResponse<Profile> successResponse = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        successResponse.setResult(GsonUtil.fromJson(profileModel.getProfile().toString(), Profile.class));

        return successResponse;
    }

    /**
     * Update user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> updateUserProfile(Profile profile) {

        if (profile == null || StringUtil.isNullOrEmpty(profile.getUid())) {
            GenieResponse<Profile> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            logGEError(genieResponse, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateUserProfile@UserServiceImpl", new HashMap(), "Unable to update profile");
            return genieResponse;
        }

        if (!profile.isValid()) {
            GenieResponse<Profile> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG, Profile.class);
            logGEError(genieResponse, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateUserProfile@UserServiceImpl", new HashMap(), "Unable to update profile");
            return genieResponse;

        }
        UserProfileModel userProfileModel = UserProfileModel.buildUserProfile(mAppContext.getDBSession(), profile);
        userProfileModel.update();

        GEUpdateProfile geUpdateProfile = new GEUpdateProfile(mGameData, profile, mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(geUpdateProfile);

        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
        response.setResult(profile);

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "updateUserProfile@UserServiceImpl", new HashMap());

        return response;

    }

    /**
     * Delete user profile
     *
     * @param uid- user id
     */
    @Override
    public GenieResponse<Void> deleteUser(String uid) {

        HashMap params = new HashMap();
        params.put("uid", uid);

        //get the current user id
        UserSessionModel userSession = UserSessionModel.findUserSession(mAppContext);
        if (userSession != null) {
            if (userSession.getUserSessionBean().getUid().equals(uid)) {
                setAnonymousUser();
            }
        }
        final ContentAccessesModel accessesModel = ContentAccessesModel.findByUid(mAppContext.getDBSession(), uid);
        final UserProfileModel userProfileModel = UserProfileModel.findUserProfile(mAppContext.getDBSession(), uid);
        if (userProfileModel == null) {
            GenieResponse<Void> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, "updateUserProfile@deleteUser", params, "Unable to delete profile");
            return genieResponse;
        } else {
            final UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);

            Profile profile = new Profile("", "", "");
            profile.setUid(uid);
            final GEDeleteProfile geDeleteProfile = new GEDeleteProfile(mGameData, profile);
            mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
                @Override
                public Void perform(IDBSession dbSession) {
                    if (accessesModel != null) {
                        accessesModel.delete();
                    }
                    userProfileModel.delete();

                    userModel.delete();
                    TelemetryLogger.log(geDeleteProfile);

                    return null;
                }
            });

            GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
            TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "deleteUser@UserServiceImpl", params);

            return response;
        }

    }

    private String createAnonymousUser() {
        //random user id generated
        String uid = UUID.randomUUID().toString();
        UserModel user = UserModel.build(mAppContext.getDBSession(), uid);
        user.save();
        return user.getUid();
    }

    /**
     * set anonymous user
     */
    @Override
    public GenieResponse<String> setAnonymousUser() {
        String uid = getAnonymousUser().getResult().getUid();
        setCurrentUser(uid);
        GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, String.class);
        response.setResult(uid);
        if (response.getStatus()) {
            TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "setAnonymousUser@UserServiceImpl", new HashMap());
        } else {
            TelemetryLogger.logFailure(mAppContext, response, TAG, "setAnonymousUser@deleteUser", new HashMap(), "Unable to setAnonymous user");
        }
        return response;
    }

    /**
     * Get anonymous user data
     */
    @Override
    public GenieResponse<Profile> getAnonymousUser() {
        String anonymousUserQuery = "select u.uid from users u left join profiles p on p.uid=u.uid where p.uid is null and u.uid is not null";
        CustomReaderModel customReaderModel = CustomReaderModel.find(mAppContext.getDBSession(), anonymousUserQuery);

        String uid = null;
        if (customReaderModel == null) {
            uid = createAnonymousUser();
        } else {
            uid = customReaderModel.getData();
        }
        Profile profile = new Profile(uid);

        GenieResponse response = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        response.setResult(profile);

        if (response.getStatus()) {
            TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "getAnonymousUser@UserServiceImpl", new HashMap());
        } else {
            TelemetryLogger.logFailure(mAppContext, response, TAG, "getAnonymousUser@UserServiceImpl", new HashMap(), "Unable to get anonymous user");
        }
        return response;
    }

    /**
     * set current user
     *
     * @param uid - User id
     */
    @Override
    public GenieResponse<Void> setCurrentUser(String uid) {
        HashMap params = new HashMap();
        params.put("uid", uid);

        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);
        if (userModel == null) {
            GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_USER, ServiceConstants.ErrorMessage.NO_USER_WITH_SPECIFIED_ID, TAG, Void.class);
            logGEError(response, "setCurrentUser");
            TelemetryLogger.logFailure(mAppContext, response, TAG, "setCurrentUser@UserServiceImpl", params, "Unable to get anonymous user");
            return response;
        }

        UserSessionModel session = UserSessionModel.findUserSession(mAppContext);
        boolean sessionCreationRequired = false;
        if (session == null) {
            sessionCreationRequired = true;
        } else if (!session.getUserSessionBean().getUid().equals(uid)) {
            session.endSession();
            GESessionEnd geSessionEnd = new GESessionEnd(mGameData, session.getUserSessionBean(), mAppContext.getDeviceInfo().getDeviceID());
            TelemetryLogger.log(geSessionEnd);
            sessionCreationRequired = true;
        }

        if (sessionCreationRequired) {
            UserSessionModel userSessionModel = UserSessionModel.buildUserSession(mAppContext, uid);
            userSessionModel.startSession();
            GESessionStart geSessionEnd = new GESessionStart(mGameData, userSessionModel.getUserSessionBean(), mAppContext.getLocationInfo().getLocation(), mAppContext.getDeviceInfo().getDeviceID());
            TelemetryLogger.log(geSessionEnd);
        }
        GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "setCurrentUser@UserServiceImpl", params);


        return response;
    }

    /**
     * Get current use data
     */
    @Override
    public GenieResponse<Profile> getCurrentUser() {
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        //This should not happen if the calling app has set an anonymous user during launch. If they have not, we will create and set an anonymous user as the current user.
        if (userSessionModel == null) {
            setAnonymousUser();
            userSessionModel = UserSessionModel.findUserSession(mAppContext);
        }

        UserProfileModel userProfileModel = UserProfileModel.findUserProfile(mAppContext.getDBSession(), userSessionModel.getUserSessionBean().getUid());
        Profile profile = null;
        if (userProfileModel == null) {
            profile = new Profile(userSessionModel.getUserSessionBean().getUid());
        } else {
            profile = userProfileModel.getProfile();
        }
        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        response.setResult(profile);

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "getCurrentUser@UserServiceImpl", new HashMap());


        return response;
    }

    @Override
    public GenieResponse<UserSession> getCurrentUserSession() {
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        //This should not happen if the calling app has set an anonymous user during launch. If they have not, we will create and set an anonymous user as the current user.
        if (userSessionModel == null) {
            setAnonymousUser();
            userSessionModel = UserSessionModel.findUserSession(mAppContext);
        }
        GenieResponse<UserSession> response = GenieResponseBuilder.getSuccessResponse("");
        response.setResult(userSessionModel.getUserSessionBean());

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "getCurrentUserSession@UserServiceImpl", new HashMap());
        return response;
    }

    @Override
    public GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessCriteria criteria) {
        String isContentIdentifier = null;
        String isUid = null;
        String isContentType = null;

        if (criteria != null) {
            if (!StringUtil.isNullOrEmpty(criteria.getContentIdentifier())) {
                isContentIdentifier = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, criteria.getContentIdentifier());
            }

            if (!StringUtil.isNullOrEmpty(criteria.getUid())) {
                isUid = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_UID, criteria.getUid());
            }

            String contentTypes;
            if (criteria.getContentTypes() != null) {
                List<String> contentTypeList = new ArrayList<>();
                for (ContentType contentType : criteria.getContentTypes()) {
                    contentTypeList.add(contentType.getValue());
                }
                contentTypes = StringUtil.join("','", contentTypeList);
            } else {
                contentTypes = StringUtil.join("','", ContentType.values());
            }
            isContentType = String.format(Locale.US, "%s in ('%s')", ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, contentTypes);
        }

        String filter = null;
        if (!StringUtil.isNullOrEmpty(isContentIdentifier) && !StringUtil.isNullOrEmpty(isUid)) {
            filter = String.format(Locale.US, " where (%s AND %s AND %s)", isContentIdentifier, isUid, isContentType);
        } else if (!StringUtil.isNullOrEmpty(isContentIdentifier)) {
            filter = String.format(Locale.US, " where (%s AND %s)", isContentIdentifier, isContentType);
        } else if (!StringUtil.isNullOrEmpty(isUid)) {
            filter = String.format(Locale.US, " where (%s AND %s)", isUid, isContentType);
        }

        ContentAccessesModel contentAccessesModel = null;
        if (filter != null) {
            contentAccessesModel = ContentAccessesModel.find(mAppContext.getDBSession(), filter);
        }

        List<ContentAccess> contentAccessList = new ArrayList<>();
        if (contentAccessesModel != null) {
            for (ContentAccessModel contentAccessModel : contentAccessesModel.getContentAccessModelList()) {
                ContentAccess contentAccess = new ContentAccess();

                contentAccess.setIdentifier(contentAccessModel.getIdentifier());
                contentAccess.setUid(contentAccessModel.getUid());
                contentAccess.setStatus(contentAccessModel.getStatus());
                contentAccess.setLearnerState(GsonUtil.fromJson(contentAccessModel.getLearnerStateJson(), Map.class));

                contentAccessList.add(contentAccess);
            }
        }

        GenieResponse<List<ContentAccess>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentAccessList);

        TelemetryLogger.logSuccess(mAppContext, response, new HashMap(), TAG, "getAllContentAccess@UserServiceImpl", new HashMap());


        return response;
    }

    @Override
    public GenieResponse<Void> setLearnerState(String contentIdentifier, Map<String, Object> learnerState) {
        UserSession userSession = getCurrentUserSession().getResult();
        String uid = userSession.getUid();
        if (StringUtil.isNullOrEmpty(uid)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, "Failed to get the current user id.", TAG);
        }

        ContentAccessModel contentAccessModel = ContentAccessModel.build(mAppContext.getDBSession(), uid, contentIdentifier, GsonUtil.toJson(learnerState));
        ContentAccessModel contentAccessModelInDb = ContentAccessModel.find(mAppContext.getDBSession(), uid, contentIdentifier);
        if (contentAccessModelInDb == null) {
            contentAccessModel.setStatus(ServiceConstants.ContentAccessStatus.VIEWED);
            contentAccessModel.save();
        } else {
            contentAccessModel.setStatus(contentAccessModelInDb.getStatus());
            contentAccessModel.update();
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

}