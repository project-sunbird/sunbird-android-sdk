package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentLearnerState;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatus;
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
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.chained.export.AddGeTransferProfileExportEvent;
import org.ekstep.genieservices.profile.chained.export.CleanupExportedFile;
import org.ekstep.genieservices.profile.chained.export.CopyDatabase;
import org.ekstep.genieservices.profile.chained.export.CreateMetadata;
import org.ekstep.genieservices.profile.chained.imports.AddGeTransferProfileImportEvent;
import org.ekstep.genieservices.profile.chained.imports.TransportProfiles;
import org.ekstep.genieservices.profile.chained.imports.TransportSummarizer;
import org.ekstep.genieservices.profile.chained.imports.TransportUser;
import org.ekstep.genieservices.profile.chained.imports.UpdateImportedProfileMetadata;
import org.ekstep.genieservices.profile.chained.imports.ValidateProfileMetadata;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.File;
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

    private GameData mGameData;

    public UserServiceImpl(AppContext appContext) {
        super(appContext);
        mGameData = new GameData(mAppContext.getParams().getString(ServiceConstants.Params.GID), mAppContext.getParams().getString(ServiceConstants.Params.VERSION_NAME));
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
        } else if (!profile.isValid()) {
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

    private GenieResponse<Profile> saveUserProfile(final Profile profile, IDBSession dbSession) {
        String uid = profile.getUid();
        if (StringUtil.isNullOrEmpty(uid)) {
            uid = UUID.randomUUID().toString();
        }
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

    @Override
    public GenieResponse<List<Profile>> getAllUserProfile() {
        String methodName = "getAllUserProfile@UserServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "1");

        GenieResponse<List<Profile>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        UserProfilesModel userProfilesModel = UserProfilesModel.find(mAppContext.getDBSession());
        if (userProfilesModel == null) {
            response.setResult(new ArrayList<Profile>());
        } else {
            response.setResult(userProfilesModel.getProfileList());
        }
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private void logGEError(GenieResponse response, String id) {
        GEError geError = new GEError(mGameData, response.getError(), id, "", response.getErrorMessages().toString());
        TelemetryLogger.log(geError);
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

        GenieResponse<Profile> response;
        if (profile == null || StringUtil.isNullOrEmpty(profile.getUid())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            logGEError(response, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDTAE_PROFILE);
            return response;
        }

        if (!profile.isValid()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG, Profile.class);
            logGEError(response, "updateUserProfile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDTAE_PROFILE);
            return response;
        }
        UserProfileModel userProfileModel = UserProfileModel.build(mAppContext.getDBSession(), profile);
        // TODO: 6/6/2017 - check if profile exists or not before updating.
        userProfileModel.update();

        GEUpdateProfile geUpdateProfile = new GEUpdateProfile(mGameData, profile, mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(geUpdateProfile);

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
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

        GenieResponse<Void> response;

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
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_DELETE_PROFILE);
            return response;
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

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
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
        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse("", Profile.class);
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

        GenieResponse<Void> response;

        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);
        if (userModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_USER, ServiceConstants.ErrorMessage.NO_USER_WITH_SPECIFIED_ID, TAG, Void.class);
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

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
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
        Profile profile;
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

        String contentFilter = null;
        String userFilter = null;

        if (criteria != null) {
            if (!StringUtil.isNullOrEmpty(criteria.getContentId())) {
                contentFilter = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, criteria.getContentId());
            }

            if (!StringUtil.isNullOrEmpty(criteria.getUid())) {
                userFilter = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_UID, criteria.getUid());
            }

        }

        String filter = null;
        if (!StringUtil.isNullOrEmpty(contentFilter) && !StringUtil.isNullOrEmpty(userFilter)) {
            filter = String.format(Locale.US, " where (%s AND %s)", contentFilter, userFilter);
        } else if (!StringUtil.isNullOrEmpty(contentFilter)) {
            filter = String.format(Locale.US, " where (%s)", contentFilter);
        } else if (!StringUtil.isNullOrEmpty(userFilter)) {
            filter = String.format(Locale.US, " where (%s)", userFilter);
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
                Map learnerState = GsonUtil.fromJson(contentAccessModel.getLearnerStateJson(), HashMap.class);
                ContentLearnerState contentLearnerState = new ContentLearnerState();
                contentLearnerState.setLearnerState(learnerState);
                contentAccess.setContentLearnerState(contentLearnerState);

                contentAccessList.add(contentAccess);
            }
        }

        GenieResponse<List<ContentAccess>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(contentAccessList);

        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);

        return response;
    }

    @Override
    public GenieResponse<Void> addContentAccess(ContentAccess contentAccess) {
        String methodName = "setLearnerState@UserServiceImpl";
        String contentLearnerState = (contentAccess.getContentLearnerState() == null) ? null : GsonUtil.toJson(contentAccess.getContentLearnerState().getLearnerState());
        HashMap params = new HashMap();
        params.put("contentIdentifier", contentAccess.getContentId());
        params.put("learnerState", contentLearnerState);
        params.put("logLevel", "2");

        GenieResponse<Void> response;

        UserSession userSession = getCurrentUserSession().getResult();
        String uid = userSession.getUid();
        if (StringUtil.isNullOrEmpty(uid)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_SET_CURRENT_USER);
            return response;
        }

        ContentAccessModel contentAccessModelInDb = ContentAccessModel.find(mAppContext.getDBSession(), uid, contentAccess.getContentId());
        if (contentAccessModelInDb == null) {
            ContentAccessModel contentAccessModel = ContentAccessModel.build(mAppContext.getDBSession(),
                    uid, contentAccess.getContentId(), contentLearnerState);
            contentAccessModel.setStatus(ContentAccessStatus.PLAYED.getValue());

            contentAccessModel.save();
        } else {
            contentAccessModelInDb.setStatus(contentAccessModelInDb.getStatus());
            if (contentLearnerState != null)
                contentAccessModelInDb.setLearnerStateJson(contentLearnerState);
            contentAccessModelInDb.update();
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<ProfileImportResponse> importProfile(IDBSession dbSession, Map<String, Object> metadata) {
        ImportContext importContext = new ImportContext(dbSession, metadata);

        ValidateProfileMetadata validateProfileMetadata = new ValidateProfileMetadata();
        validateProfileMetadata.then(new TransportProfiles())
                .then(new TransportUser())
                .then(new TransportSummarizer())
                .then(new UpdateImportedProfileMetadata())
                .then(new AddGeTransferProfileImportEvent());

        return validateProfileMetadata.execute(mAppContext, importContext);
    }

    @Override
    public GenieResponse<ProfileExportResponse> exportProfile(List<String> userIds, File destinationFolder, String sourceDBFilePath, String destinationDBFilePath, IDataSource dataSource, Map<String, Object> metadata) {
        if (FileUtil.doesFileExists(destinationDBFilePath)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "File already exists.", TAG);
        }

        ImportContext importContext = new ImportContext(null, metadata);

        CopyDatabase copyDatabase = new CopyDatabase(sourceDBFilePath, destinationDBFilePath, dataSource);
        copyDatabase.then(new CreateMetadata(destinationDBFilePath, userIds))
                .then(new CleanupExportedFile(destinationDBFilePath, userIds))
                .then(new AddGeTransferProfileExportEvent(destinationDBFilePath));

        // TODO: 6/12/2017 - if export failed.
//                .then(new RemoveExportFile(destinationDBFilePath));

        return copyDatabase.execute(mAppContext, importContext);
    }

}