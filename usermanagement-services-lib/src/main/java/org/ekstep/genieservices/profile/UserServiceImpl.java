package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.AddUpdateGroupsRequest;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentLearnerState;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GetProfileRequest;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.ProfileRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.ContentAccessStatus;
import org.ekstep.genieservices.commons.bean.enums.UserSource;
import org.ekstep.genieservices.commons.bean.telemetry.Actor;
import org.ekstep.genieservices.commons.bean.telemetry.Audit;
import org.ekstep.genieservices.commons.bean.telemetry.End;
import org.ekstep.genieservices.commons.bean.telemetry.Error;
import org.ekstep.genieservices.commons.bean.telemetry.Start;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.model.CustomReaderModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.importexport.bean.ExportProfileContext;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.chained.export.AddGeTransferProfileExportEvent;
import org.ekstep.genieservices.profile.chained.export.CleanupExportedFile;
import org.ekstep.genieservices.profile.chained.export.CopyDatabase;
import org.ekstep.genieservices.profile.chained.export.CreateMetadata;
import org.ekstep.genieservices.profile.chained.imports.AddGeTransferProfileImportEvent;
import org.ekstep.genieservices.profile.chained.imports.TransportFrameworknChannel;
import org.ekstep.genieservices.profile.chained.imports.TransportGroup;
import org.ekstep.genieservices.profile.chained.imports.TransportGroupProfile;
import org.ekstep.genieservices.profile.chained.imports.TransportProfiles;
import org.ekstep.genieservices.profile.chained.imports.TransportSummarizer;
import org.ekstep.genieservices.profile.chained.imports.TransportUser;
import org.ekstep.genieservices.profile.chained.imports.UpdateImportedProfileMetadata;
import org.ekstep.genieservices.profile.chained.imports.ValidateProfileMetadata;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.db.model.GroupProfilesModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    public static final String DATE_FORMAT = "yyyyMMddhhmmss";
    private static final String TAG = UserServiceImpl.class.getSimpleName();
    private IUserProfileService mUserProfileService;

    public UserServiceImpl(AppContext appContext, IUserProfileService userProfileService) {
        super(appContext);
        this.mUserProfileService = userProfileService;
    }

    private static List<String> findProfilePropDiff(Profile firstInstance, Profile secondInstance) {
        List<String> changedProps = new ArrayList<>();
        try {
            if (firstInstance != null && secondInstance != null) {
                if ((firstInstance.getHandle() != null && !firstInstance.getHandle().equals(secondInstance.getHandle()))
                        || (firstInstance.getHandle() == null && secondInstance.getHandle() != null)) {
                    changedProps.add("handle");
                }
                if ((firstInstance.getAvatar() != null && !firstInstance.getAvatar().equals(secondInstance.getAvatar()))
                        || (firstInstance.getAvatar() == null && secondInstance.getAvatar() != null)) {
                    changedProps.add("avatar");
                }
                if ((firstInstance.getGender() != null && !firstInstance.getGender().equals(secondInstance.getGender()))
                        || (firstInstance.getGender() == null && secondInstance.getGender() != null)) {
                    changedProps.add("gender");
                }
                if (!(firstInstance.getAge() == secondInstance.getAge())) {
                    changedProps.add("age");
                }
                if (!(firstInstance.getDay() == secondInstance.getDay())) {
                    changedProps.add("day");
                }
                if (!(firstInstance.getMonth() == secondInstance.getMonth())) {
                    changedProps.add("month");
                }
                if (!(firstInstance.getStandard() == secondInstance.getStandard())) {
                    changedProps.add("standard");
                }
                if ((firstInstance.getMedium() != null && !Arrays.equals(firstInstance.getMedium(), secondInstance.getMedium()))
                        || (firstInstance.getMedium() == null && secondInstance.getMedium() != null)) {
                    changedProps.add("medium");
                }
                if ((firstInstance.getBoard() != null && !Arrays.equals(firstInstance.getBoard(), secondInstance.getBoard()))
                        || firstInstance.getBoard() == null && secondInstance.getBoard() != null) {
                    changedProps.add("board");
                }
                if (!(firstInstance.isGroupUser() == secondInstance.isGroupUser())) {
                    changedProps.add("isGroupUser");
                }
                if ((firstInstance.getCreatedAt() != null && !firstInstance.getCreatedAt().equals(secondInstance.getCreatedAt()))
                        || (firstInstance.getCreatedAt() == null && secondInstance.getCreatedAt() != null)) {
                    changedProps.add("createdAt");
                }
                if ((firstInstance.getProfileImage() != null && !firstInstance.getProfileImage().equals(secondInstance.getProfileImage()))
                        || (firstInstance.getProfileImage() == null && secondInstance.getProfileImage() != null)) {
                    changedProps.add("profileImage");
                }
                if ((firstInstance.getProfileType() != null && firstInstance.getProfileType().equals(secondInstance.getProfileType()))
                        || (firstInstance.getProfileType() == null && secondInstance.getProfileType() != null)) {
                    changedProps.add("profileType");
                }
                if ((firstInstance.getSubject() != null && !Arrays.equals(firstInstance.getSubject(), secondInstance.getSubject()))
                        || (firstInstance.getSubject() == null && secondInstance.getSubject() != null)) {
                    changedProps.add("subject");
                }
                if (firstInstance.getGrade() != null && (!Arrays.equals(firstInstance.getGrade(), secondInstance.getGrade()))
                        || (firstInstance.getGrade() == null && secondInstance.getGrade() != null)) {
                    changedProps.add("grade");
                }
            }
        } catch (Exception e) {

        }

        return changedProps;
    }

    public List<String> findAvailableProps(Profile profile) {
        List<String> availableFields = new ArrayList<>();
        if (profile != null) {
            if (!StringUtil.isNullOrEmpty(profile.getHandle())) {
                availableFields.add("handle");
            }

            if (!StringUtil.isNullOrEmpty(profile.getAvatar())) {
                availableFields.add("avatar");
            }

            if (!StringUtil.isNullOrEmpty(profile.getGender()))
                availableFields.add("gender");

            if (profile.getAge() != -1) {
                availableFields.add("age");
            }
            if (profile.getDay() != -1) {
                availableFields.add("day");
            }
            if (profile.getMonth() != -1) {
                availableFields.add("month");
            }
            if (profile.getStandard() != -1) {
                availableFields.add("standard");
            }
            if (profile.getMedium() != null) {
                availableFields.add("medium");
            }
            if (profile.getBoard() != null) {
                availableFields.add("board");
            }
            availableFields.add("isGroupUser");
            if (profile.getCreatedAt() != null) {
                availableFields.add("createdAt");
            }
            if (!StringUtil.isNullOrEmpty(profile.getProfileImage())) {
                availableFields.add("profileImage");
            }
            if (!StringUtil.isNullOrEmpty(profile.getProfileType())) {
                availableFields.add("profileType");
            }
            if (profile.getSubject() != null) {
                availableFields.add("subject");
            }
            if (profile.getGrade() != null) {
                availableFields.add("grade");
            }
        }
        return availableFields;
    }

    /**
     * Create a new user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> createUserProfile(Profile profile) {
        String methodName = "createUserProfile@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Profile> response;
        if (profile == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_PROFILE, ServiceConstants.ErrorMessage.INVALID_PROFILE, methodName, Profile.class);
            logGEError(response, "create-user-profile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_CREATE_PROFILE);
            return response;
        } else if (!profile.isValid()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.VALIDATION_ERROR, profile.getErrors().toString(), methodName, Profile.class);
            logGEError(response, "create-user-profile");
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

        final UserProfileModel profileModel = UserProfileModel.build(dbSession, profile);
        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                userModel.save();
                logUserAuditEvent(userModel.getUid());
                profileModel.save();
                logProfileAuditEvent(profileModel.getProfile(), null);
                return null;
            }
        });

        GenieResponse<Profile> successResponse = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        successResponse.setResult(GsonUtil.fromJson(profileModel.getProfile().toString(), Profile.class));
        return successResponse;
    }

    private void logUserAuditEvent(String uid) {
        Audit.Builder audit = new Audit.Builder();
        audit.currentState(ServiceConstants.Telemetry.AUDIT_CREATED)
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_USER)
                .objectId(uid)
                .actorType(Actor.TYPE_SYSTEM).actorId(mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(audit.build());
    }

    private void logProfileAuditEvent(Profile profile, Profile updatedProfile) {

        Audit.Builder audit = new Audit.Builder();
        audit.currentState(updatedProfile == null ? ServiceConstants.Telemetry.AUDIT_CREATED : ServiceConstants.Telemetry.AUDIT_UPDATED)
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .updatedProperties(updatedProfile == null ? findAvailableProps(profile) : findProfilePropDiff(profile, updatedProfile))
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_USER)
                .objectId(profile.getUid())
                .actorType(Actor.TYPE_SYSTEM).actorId(mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(audit.build());
    }


    private void logProfileDeleteAuditEvent(Profile profile) {
        Audit.Builder audit = new Audit.Builder();
        audit.currentState(ServiceConstants.Telemetry.AUDIT_DELETED)
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .objectType(ServiceConstants.Telemetry.OBJECT_TYPE_USER)
                .objectId(profile.getUid())
                .actorType(Actor.TYPE_SYSTEM).actorId(mAppContext.getDeviceInfo().getDeviceID());
        TelemetryLogger.log(audit.build());
    }

    @Override
    public GenieResponse<List<Profile>> getAllUserProfile() {
        String methodName = "getAllUserProfile@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
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
        Error.Builder error = new Error.Builder();
        error.errorCode(response.getError())
                .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                .errorType(Error.Type.MOBILE_APP)
                .stacktrace(response.getErrorMessages().toString())
                .pageId(id);
        TelemetryLogger.log(error.build());
    }

    /**
     * Update user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> updateUserProfile(Profile profile) {
        String methodName = "updateUserProfile@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Profile> response;
        if (profile == null || StringUtil.isNullOrEmpty(profile.getUid())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            logGEError(response, "update-user-profile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_PROFILE);
            return response;
        }

        if (!profile.isValid()) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG, Profile.class);
            logGEError(response, "update-user-profile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_PROFILE);
            return response;
        }

        UserProfileModel userProfileDbModel = UserProfileModel.find(mAppContext.getDBSession(), profile.getUid());
        if (userProfileDbModel != null) {
            UserProfileModel userProfileModel = UserProfileModel.build(mAppContext.getDBSession(), profile);
            userProfileModel.update();

            logProfileAuditEvent(userProfileDbModel.getProfile(), profile);
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
            response.setResult(profile);

            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Profile.class);
            logGEError(response, "update-user-profile");
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_UPDATE_PROFILE);
            return response;
        }
    }

    /**
     * Delete user profile
     *
     * @param uid- user id
     */
    @Override
    public GenieResponse<Void> deleteUser(String uid) {
        String methodName = "deleteUser@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
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
//  TODO:      final ContentMarkersModel accessesModel = ContentAccessesModel.findByUid(mAppContext.getDBSession(), uid);
        final GroupProfilesModel groupProfilesModel = GroupProfilesModel.findByUid(mAppContext.getDBSession(), uid);

        final UserProfileModel userProfileModel = UserProfileModel.find(mAppContext.getDBSession(), uid);
        if (userProfileModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROFILE_NOT_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PROFILE, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_DELETE_PROFILE);
            return response;
        } else {
            final UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);

            Profile profile = new Profile("", "", "");
            profile.setUid(uid);
            final Profile profileDB = userProfileModel.getProfile();
            mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
                @Override
                public Void perform(IDBSession dbSession) {
                    if (accessesModel != null) {
                        accessesModel.delete();
                    }

                    if (groupProfilesModel != null) {
                        groupProfilesModel.delete();
                    }

                    userProfileModel.delete();

                    userModel.delete();
                    logProfileDeleteAuditEvent(profileDB);
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
        Map<String, Object> params = new HashMap<>();
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
            logUserAuditEvent(uid);
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
        Map<String, Object> params = new HashMap<>();
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
            End end = new End.Builder()
                    .type(ServiceConstants.Telemetry.SESSION)
                    .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                    .duration(DateUtil.elapsedTimeTillNow(session.getUserSessionBean().getCreatedTime()))
                    .build();
            TelemetryLogger.log(end);
            session.endSession();
            sessionCreationRequired = true;
        }

        if (sessionCreationRequired) {
            UserSessionModel userSessionModel = UserSessionModel.buildUserSession(mAppContext, uid);
            userSessionModel.startSession();
            Start start = new Start.Builder().
                    type(ServiceConstants.Telemetry.SESSION)
                    .loc(mAppContext.getLocationInfo().getLocation())
                    .environment(ServiceConstants.Telemetry.SDK_ENVIRONMENT)
                    .build();
            TelemetryLogger.log(start);
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
        Map<String, Object> params = new HashMap<>();
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


        // This will add Logged in User information
        UserProfileDetailsRequest.Builder builder = new UserProfileDetailsRequest.Builder().forUser(profile.getUid());
        UserProfile userProfile = mUserProfileService.getUserProfileDetails(builder.build()).getResult();
        if (userProfile != null) {
            Map map = GsonUtil.fromJson(userProfile.getUserProfile(), Map.class);
            String firstName = String.valueOf(map.get("firstName"));
            profile.setHandle((!StringUtil.isNullOrEmpty(firstName) ? firstName : "") + " " + (map.get("lastName") != null ? String.valueOf(map.get("lastName")) : ""));
        }

        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(profile);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<UserSession> getCurrentUserSession() {
        String methodName = "getCurrentUserSession@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "1");

        UserSessionModel userSessionModel = UserSessionModel.findUserSession(mAppContext);

        //This should not happen if the calling app has set an anonymous user during launch. If they have not, we will create and set an anonymous user as the current user.
        if (userSessionModel == null) {
            initAnonymousUser();
            userSessionModel = UserSessionModel.findUserSession(mAppContext);
        }

        GenieResponse<UserSession> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        if (userSessionModel != null) {
            response.setResult(userSessionModel.getUserSessionBean());
        }
        return response;
    }

    private void initAnonymousUser() {
        String uid = getAnonymousUserId();
        if (uid == null) {
            uid = createAnonymousUser();
            logUserAuditEvent(uid);
            setCurrentUser(uid);
        }
    }

    @Override
    public GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessFilterCriteria criteria) {
        String methodName = "getAllContentAccess@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
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
        Map<String, Object> params = new HashMap<>();
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
                    uid, contentAccess.getContentId(), contentAccess.getContentType(), contentLearnerState);
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
    public GenieResponse<ProfileImportResponse> importProfile(ProfileImportRequest profileImportRequest) {
        GenieResponse<ProfileImportResponse> response;
        if (!FileUtil.doesFileExists(profileImportRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE,
                    "Profile import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(profileImportRequest.getSourceFilePath());
        if (ServiceConstants.FileExtension.PROFILE.equals(ext)) {
            ImportProfileContext importProfileContext = new ImportProfileContext(profileImportRequest.getSourceFilePath());
            ValidateProfileMetadata validateProfileMetadata = new ValidateProfileMetadata();
            validateProfileMetadata.then(new TransportProfiles())
                    .then(new TransportUser())
                    .then(new TransportGroup())
                    .then(new TransportGroupProfile())
                    .then(new TransportFrameworknChannel())
                    .then(new TransportSummarizer())
                    .then(new UpdateImportedProfileMetadata())
                    .then(new AddGeTransferProfileImportEvent());

            return validateProfileMetadata.execute(mAppContext, importProfileContext);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE,
                    "Profile import failed, unsupported file extension", TAG);
            return response;
        }
    }

    @Override
    public GenieResponse<ProfileExportResponse> exportProfile(ProfileExportRequest profileExportRequest) {
        if (CollectionUtil.isNullOrEmpty(profileExportRequest.getUserIds())) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED,
                    "There are no profile to export.", TAG);
        }

        File destinationFolder = new File(profileExportRequest.getDestinationFolder());

        // Read the first profile and get the destination DB file path
        String destinationDBFilePath = getEparFilePath(profileExportRequest.getGroupIds(), profileExportRequest.getUserIds(), destinationFolder);

        if (FileUtil.doesFileExists(destinationDBFilePath)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED,
                    "File already exists.", TAG);
        }

        ExportProfileContext exportProfileContext = new ExportProfileContext(profileExportRequest.getUserIds(),
                profileExportRequest.getGroupIds(), profileExportRequest.getDestinationFolder(), destinationDBFilePath);
        CopyDatabase copyDatabase = new CopyDatabase();
        copyDatabase.then(new CreateMetadata())
                .then(new CleanupExportedFile())
                .then(new AddGeTransferProfileExportEvent());

        // TODO: 6/12/2017 - if export failed.
//                .then(new RemoveExportFile(destinationDBFilePath));

        return copyDatabase.execute(mAppContext, exportProfileContext);
    }

    private String getEparFilePath(List<String> groupIds, List<String> userIds, File destinationFolder) {
//        String fileName = "profile." + ServiceConstants.FileExtension.PROFILE;
//        String groupName = null;
//        if (!CollectionUtil.isNullOrEmpty(groupIds)) {
//            GroupModel groupModel = GroupModel.findGroupById(mAppContext.getDBSession(), groupIds.get(0));
//            Group group = groupModel.getGroup();
//            groupName = group.getName();
//        }
//        UserProfileModel userProfileModel = UserProfileModel.find(mAppContext.getDBSession(), userIds.get(0));
//        if (userProfileModel != null) {
//            Profile firstProfile = userProfileModel.getProfile();
//            String profileName = firstProfile.getHandle();
//            String appendName = "";
//            if (!StringUtil.isNullOrEmpty(groupName)) {
//                appendName = String.format(Locale.US, "+%s", ((groupIds.size() - 1) + (userIds.size())));
//                fileName = String.format(Locale.US, "%s%s." + ServiceConstants.FileExtension.PROFILE, groupName, appendName);
//            } else {
//                if (userIds.size() > 1) {
//                    appendName = String.format(Locale.US, "+%s", (userIds.size() - 1));
//                }
//                fileName = String.format(Locale.US, "%s%s." + ServiceConstants.FileExtension.PROFILE, profileName, appendName);
//            }
//
//        }
        String fileName = "profiles_" + DateUtil.format(new Date().getTime(), DATE_FORMAT) + "." + ServiceConstants.FileExtension.PROFILE;
        File eparFile = FileUtil.getTempLocation(destinationFolder, fileName);
        if (eparFile.exists()) {
            try {
                eparFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return eparFile.getAbsolutePath();
    }

    @Override
    public GenieResponse<List<Profile>> getAllUserProfile(ProfileRequest profileRequest) {
        String methodName = "getAllUserProfile@UserServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "1");

        String localUserFilter = null;
        String serverUserFilter = null;
        String groupFilter = null;

        if (profileRequest != null) {
            //filter for local users
            if (profileRequest.isLocal()) {
                localUserFilter = String.format(Locale.US, "%s = '%s'", ProfileEntry.COLUMN_SOURCE, UserSource.LOCAL.getValue());
            }

            //filter for server users
            if (profileRequest.isServer()) {
                serverUserFilter = String.format(Locale.US, "%s = '%s'", ProfileEntry.COLUMN_SOURCE, UserSource.SERVER.getValue());
            }

            //get the gids
            if (!StringUtil.isNullOrEmpty(profileRequest.getGroupId())) {
                groupFilter = String.format(Locale.US, "SELECT %s FROM %s WHERE %s = '%s'",
                        GroupProfileEntry.COLUMN_NAME_UID, GroupProfileEntry.TABLE_NAME, GroupProfileEntry.COLUMN_NAME_GID, profileRequest.getGroupId());
            }

        }

        String filter = null;
        if (!StringUtil.isNullOrEmpty(localUserFilter) && !StringUtil.isNullOrEmpty(serverUserFilter) && !StringUtil.isNullOrEmpty(groupFilter)) {
            filter = String.format(Locale.US, " where %s OR %s AND %s IN (%s)", localUserFilter, serverUserFilter, ProfileEntry.COLUMN_NAME_UID, groupFilter);
        } else if (!StringUtil.isNullOrEmpty(localUserFilter) && !StringUtil.isNullOrEmpty(groupFilter)) {
            filter = String.format(Locale.US, " where %s AND %s IN (%s)", localUserFilter, ProfileEntry.COLUMN_NAME_UID, groupFilter);
        } else if (!StringUtil.isNullOrEmpty(serverUserFilter) && !StringUtil.isNullOrEmpty(groupFilter)) {
            filter = String.format(Locale.US, " where %s AND %s IN (%s)", serverUserFilter, ProfileEntry.COLUMN_NAME_UID, groupFilter);
        } else if (!StringUtil.isNullOrEmpty(localUserFilter) && !StringUtil.isNullOrEmpty(serverUserFilter)) {
            filter = String.format(Locale.US, " where (%s OR %s)", localUserFilter, serverUserFilter);
        } else if (!StringUtil.isNullOrEmpty(localUserFilter)) {
            filter = String.format(Locale.US, " where (%s)", localUserFilter);
        } else if (!StringUtil.isNullOrEmpty(serverUserFilter)) {
            filter = String.format(Locale.US, " where (%s)", serverUserFilter);
        }


        GenieResponse<List<Profile>> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        UserProfilesModel userProfilesModel = UserProfilesModel.find(mAppContext.getDBSession(), filter);
        if (userProfilesModel == null) {
            response.setResult(new ArrayList<Profile>());
        } else {
            response.setResult(userProfilesModel.getProfileList());
        }
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    @Override
    public GenieResponse<Void> addUpdateGroupsToProfile(AddUpdateGroupsRequest addUpdateGroupsRequest) {
        // TODO: 20/7/18 Yet to be implemented
        return null;
    }

    @Override
    public GenieResponse<Profile> getProfile(GetProfileRequest getProfileRequest) {
        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);

        String localUserFilter = null;
        String serverUserFilter = null;

        if (getProfileRequest != null) {
            //filter for local users
            if (getProfileRequest.isLocal()) {
                localUserFilter = String.format(Locale.US, "%s = '%s'", ProfileEntry.COLUMN_SOURCE, UserSource.LOCAL.getValue());
            }

            //filter for server users
            if (getProfileRequest.isServer()) {
                serverUserFilter = String.format(Locale.US, "%s = '%s'", ProfileEntry.COLUMN_SOURCE, UserSource.SERVER.getValue());
            }
        }

        String filter = null;
        if (!StringUtil.isNullOrEmpty(localUserFilter) && !StringUtil.isNullOrEmpty(serverUserFilter)) {
            filter = String.format(Locale.US, " where %s OR %s", localUserFilter, serverUserFilter);
        } else if (!StringUtil.isNullOrEmpty(localUserFilter)) {
            filter = String.format(Locale.US, " where %s", localUserFilter);
        } else if (!StringUtil.isNullOrEmpty(serverUserFilter)) {
            filter = String.format(Locale.US, " where %s", serverUserFilter);
        }

        if (getProfileRequest.isLatestCreatedProfile()) {
            UserProfilesModel userProfilesModel;

            if (StringUtil.isNullOrEmpty(filter)) {
                userProfilesModel = UserProfilesModel.find(mAppContext.getDBSession(), "", true);
            } else {
                userProfilesModel = UserProfilesModel.find(mAppContext.getDBSession(), filter, true);
            }

            if (userProfilesModel != null) {
                response.setResult(userProfilesModel.getProfileList().get(0));
            }
        } else {
            String filterWithUid = null;

            if (StringUtil.isNullOrEmpty(filter)) {
                filterWithUid = String.format(Locale.US, " where %s = '%s'", ProfileEntry.COLUMN_NAME_UID, getProfileRequest.getUid());
            } else {
                filterWithUid = String.format(Locale.US, " where %s AND %s = '%s'", filter, ProfileEntry.COLUMN_NAME_UID, getProfileRequest.getUid());
            }

            UserProfileModel userProfileModel = UserProfileModel.find(mAppContext.getDBSession(), getProfileRequest.getUid(), filterWithUid);

            if (userProfileModel != null) {
                response.setResult(userProfileModel.getProfile());
            }

        }

        return response;
    }
}