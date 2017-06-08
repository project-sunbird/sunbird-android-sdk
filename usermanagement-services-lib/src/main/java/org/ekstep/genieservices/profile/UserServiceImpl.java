package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentAccessLearnerState;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatusType;
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
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;
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
        String methodName = "createUserProfile@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");

        GenieResponse<Profile> response;
        if (profile == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_PROFILE, ServiceConstants.ErrorMessage.INVALID_PROFILE, methodName, Profile.class);
            logGEError(response, "createUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_CREATE_PROFILE);
            return response;
        } else if (profile != null && !profile.isValid()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, profile.getErrors().toString(), methodName, Profile.class);
            logGEError(response, "createUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_CREATE_PROFILE);
            return response;
        } else {
            response = saveUserProfile(profile, mAppContext.getDBSession());
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        }
    }

    @Override
    public GenieResponse<List<Profile>> getAllUserProfile() {
        String methodName = "getAllUserProfile@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");
        UserProfilesModel userProfilesModel = UserProfilesModel.find(mAppContext.getDBSession());

        if (userProfilesModel == null) {
            GenieResponse genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.DATA_NOT_FOUND_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_ALL_PROFILE);
            return genieResponse;
        } else {
            GenieResponse genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, List.class);
            genieResponse.setResult(userProfilesModel.getProfileList());
            TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);
            return genieResponse;
        }

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
        final UserProfileModel profileModel = UserProfileModel.build(dbSession, profile);
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
        GenieResponse<Profile> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
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

        String methodName = "updateUserProfile@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");

        if (profile == null || StringUtil.isNullOrEmpty(profile.getUid())) {
            GenieResponse<Profile> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            logGEError(genieResponse, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDTAE_PROFILE);
            return genieResponse;
        }

        if (!profile.isValid()) {
            GenieResponse<Profile> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG, Profile.class);
            logGEError(genieResponse, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDTAE_PROFILE);
            return genieResponse;
        }
        UserProfileModel userProfileModel = UserProfileModel.build(mAppContext.getDBSession(), profile);
        // TODO: 6/6/2017 - check if profile exists or not before updating.
        userProfileModel.update();

        GEUpdateProfile geUpdateProfile = new GEUpdateProfile(mGameData, profile, mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(geUpdateProfile);

        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
        response.setResult(profile);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

        return response;

    }

    /**
     * Delete user profile
     *
     * @param uid- user id
     */
    @Override
    public GenieResponse<Void> deleteUser(String uid) {

        String methodName = "deleteUser@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("uid", uid);
        params.put("logLevel", "2");

        //get the current user id
        UserSessionModel userSession = UserSessionModel.findUserSession(mAppContext);
        if (userSession != null) {
            if (userSession.getUserSessionBean().getUid().equals(uid)) {
                setAnonymousUser();
            }
        }
        final ContentAccessesModel accessesModel = ContentAccessesModel.findByUid(mAppContext.getDBSession(), uid);
        final UserProfileModel userProfileModel = UserProfileModel.find(mAppContext.getDBSession(), uid);
        if (userProfileModel == null) {
            GenieResponse<Void> genieResponse = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, genieResponse, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_DELETE_PROFILE);
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
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

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
        String methodName = "setAnonymousUser@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");

        String uid = getAnonymousUser().getResult().getUid();
        setCurrentUser(uid);
        GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, String.class);
        response.setResult(uid);
        return response;
    }

    /**
     * Get anonymous user data
     */
    @Override
    public GenieResponse<Profile> getAnonymousUser() {
        String uid = getAnonymousUserId();
        if (uid == null) {
            uid = createAnonymousUser();
            GECreateUser geCreateUser = new GECreateUser(mGameData, uid, mAppContext.getLocationInfo().getLocation());
            TelemetryLogger.log(geCreateUser);
        }
        Profile profile = new Profile(uid);
        GenieResponse response = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        response.setResult(profile);
        return response;
    }


    private String getAnonymousUserId() {
        String anonymousUserQuery = "select u.uid from users u left join profiles p on p.uid=u.uid where p.uid is null and u.uid is not null";
        CustomReaderModel customReaderModel = CustomReaderModel.find(mAppContext.getDBSession(), anonymousUserQuery);
        return customReaderModel != null ? customReaderModel.getData() : null;
    }

    /**
     * set current user
     *
     * @param uid - User id
     */
    @Override
    public GenieResponse<Void> setCurrentUser(String uid) {
        String methodName = "setCurrentUser@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("uid", uid);
        params.put("logLevel", "2");

        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);
        if (userModel == null) {
            GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_USER, ServiceConstants.ErrorMessage.NO_USER_WITH_SPECIFIED_ID, TAG, Void.class);
            logGEError(response, "setCurrentUser");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_SET_CURRENT_USER);
            return response;
        }

        UserSessionModel session = UserSessionModel.findUserSession(mAppContext);
        boolean sessionCreationRequired = false;
        if (session == null) {
            sessionCreationRequired = true;
        } else if (!session.getUserSessionBean().getUid().equals(uid)) {
            GESessionEnd geSessionEnd = new GESessionEnd(mGameData, session.getUserSessionBean(), mAppContext.getDeviceInfo().getDeviceID());
            TelemetryLogger.log(geSessionEnd);
            session.endSession();
            sessionCreationRequired = true;
        }

        if (sessionCreationRequired) {
            UserSessionModel userSessionModel = UserSessionModel.buildUserSession(mAppContext, uid);
            userSessionModel.startSession();
            GESessionStart geSessionStart = new GESessionStart(mGameData, userSessionModel.getUserSessionBean(), mAppContext.getLocationInfo().getLocation(), mAppContext.getDeviceInfo().getDeviceID());
            TelemetryLogger.log(geSessionStart);
        }
        GenieResponse response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);


        return response;
    }

    /**
     * Get current use data
     */
    @Override
    public GenieResponse<Profile> getCurrentUser() {

        String methodName = "getCurrentUser@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");

        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        //This should not happen if the calling app has set an anonymous user during launch. If they have not, we will create and set an anonymous user as the current user.
        if (userSessionModel == null) {
            setAnonymousUser();
            userSessionModel = UserSessionModel.findUserSession(mAppContext);
        }

        UserProfileModel userProfileModel = UserProfileModel.find(mAppContext.getDBSession(), userSessionModel.getUserSessionBean().getUid());
        Profile profile = null;
        if (userProfileModel == null) {
            profile = new Profile(userSessionModel.getUserSessionBean().getUid());
        } else {
            profile = userProfileModel.getProfile();
        }
        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profile);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);


        return response;
    }

    @Override
    public GenieResponse<UserSession> getCurrentUserSession() {

        String methodName = "getCurrentUserSession@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");
        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        //This should not happen if the calling app has set an anonymous user during launch. If they have not, we will create and set an anonymous user as the current user.
        if (userSessionModel == null) {
            initAnonymousUser();
            userSessionModel = UserSessionModel.findUserSession(mAppContext);
        }
        GenieResponse<UserSession> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(userSessionModel.getUserSessionBean());

        return response;
    }

    private void initAnonymousUser() {
        String uid = getAnonymousUserId();
        if (uid == null) {
            uid = createAnonymousUser();
            setCurrentUser(uid);
            GECreateUser geCreateUser = new GECreateUser(mGameData, uid, mAppContext.getLocationInfo().getLocation());
            TelemetryLogger.log(geCreateUser);
        }
    }

    @Override
    public GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessFilterCriteria criteria) {

        String methodName = "getAllContentAccess@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");

        String isContentIdentifier = null;
        String isUid = null;
        String isContentType = null;

        if (criteria != null) {
            if (!StringUtil.isNullOrEmpty(criteria.getContentId())) {
                isContentIdentifier = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, criteria.getContentId());
            }

            if (!StringUtil.isNullOrEmpty(criteria.getUid())) {
                isUid = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_UID, criteria.getUid());
            }

            isContentType = String.format(Locale.US, "%s in ('%s')", ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, ContentType.getCommaSeparatedContentTypes(criteria.getContentTypes()));
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
                contentAccess.setStatus(contentAccessModel.getStatus());
                contentAccess.setLearnerState(GsonUtil.fromJson(contentAccessModel.getLearnerStateJson(), Map.class));

                contentAccessList.add(contentAccess);
            }
        }

        GenieResponse<List<ContentAccess>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentAccessList);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, "getAllContentAccess@UserServiceImpl", new HashMap());

        return response;
    }

    @Override
    public GenieResponse<Void> setLearnerState(ContentAccessLearnerState contentAccessLearnerState) {

        String methodName = "setLearnerState@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("contentIdentifier", contentAccessLearnerState.getContentId());
        params.put("learnerState", contentAccessLearnerState.getLearnerState());
        params.put("logLevel", "2");

        UserSession userSession = getCurrentUserSession().getResult();
        String uid = userSession.getUid();
        if (StringUtil.isNullOrEmpty(uid)) {
            GenieResponse response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_SET_CURRENT_USER);
            return response;
        }

        ContentAccessModel contentAccessModel = ContentAccessModel.build(mAppContext.getDBSession(),
                uid, contentAccessLearnerState.getContentId(), GsonUtil.toJson(contentAccessLearnerState.getLearnerState()));
        ContentAccessModel contentAccessModelInDb = ContentAccessModel.find(mAppContext.getDBSession(), uid, contentAccessLearnerState.getContentId());
        if (contentAccessModelInDb == null) {
            contentAccessModel.setStatus(ContentAccessStatusType.PLAYED.getValue());
            contentAccessModel.save();
        } else {
            contentAccessModel.setStatus(contentAccessModelInDb.getStatus());
            contentAccessModel.update();
        }

        GenieResponse genieResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, genieResponse, TAG, methodName, params);

        return genieResponse;
    }

    @Override
    public GenieResponse<Void> importProfile(ProfileImportRequest importRequest) {
        GenieResponse<Void> response;

//        if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
//            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, file doesn't exists", TAG);
//            return response;
//        }
//
//        String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
//        if (!ServiceConstants.FileExtension.PROFILE.equals(ext)) {
//            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, unsupported file extension", TAG);
//            return response;
//        } else {
//            ImportContext importContext = new ImportContext(importRequest.isChildContent(), importRequest.getSourceFilePath(), importRequest.getDestinationFolder());
//
//            IChainable importContentSteps = ContentImportStep.initImportContent();
//            importContentSteps.then(new DeviceMemoryCheck())
//                    .then(new ExtractEcar())
//                    .then(new ValidateEcar())
//                    .then(new ExtractPayloads())
//                    .then(new EcarCleanUp())
//                    .then(new AddGeTransferContentImportEvent());
//            GenieResponse<Void> genieResponse = importContentSteps.execute(mAppContext, importContext);
////            if (genieResponse.getStatus()) {
////                EventPublisher.postImportSuccessfull(new ImportStatus(null));
////            }
//            return genieResponse;
//        }

        return null;
    }

}