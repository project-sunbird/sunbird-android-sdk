package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.db.model.AnonymousUserModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessesModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserSessionModel;

import java.util.ArrayList;
import java.util.Date;
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

    public UserServiceImpl(AppContext appContext) {
        super(appContext);
    }

    /**
     * Create a new user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> createUserProfile(Profile profile) {
        GenieResponse<Profile> response = null;
        if (profile != null && !profile.isValid()) {
            // TODO: 26/4/17 Need to create error event
//            return logAndSendResponse(request.gameID(), request.gameVersion(), new Response("failed", "VALIDATION_ERROR",
//                    profile.getErrors(), ""), "createProfile");
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.VALIDATION_ERROR, profile.getErrors().toString(), TAG + " - createProfile", Profile.class);
        } else {
            response = saveUserProfile(profile, mAppContext.getDBSession());
        }
        return response;
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
     * Update user profile
     *
     * @param profile - User profile data
     */
    @Override
    public GenieResponse<Profile> updateUserProfile(Profile profile) {

        if (profile == null || StringUtil.isNullOrEmpty(profile.getUid())) {
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

        GenieResponse<Profile> response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Profile.class);
        response.setResult(profile);
        return response;

    }

    /**
     * Delete user profile
     *
     * @param uid- user id
     */
    @Override
    public GenieResponse<Void> deleteUser(String uid) {
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

        return GenieResponseBuilder.getSuccessResponse("", Void.class);
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
        return response;
    }

    /**
     * Get anonymous user data
     */
    @Override
    public GenieResponse<Profile> getAnonymousUser() {
        AnonymousUserModel anonymousUserModel = AnonymousUserModel.findAnonymousUser(mAppContext.getDBSession());

        String uid = null;
        if (anonymousUserModel == null) {
            uid = createAnonymousUser();
        } else {
            uid = anonymousUserModel.getUid();
        }
        Profile profile = new Profile(uid);

        GenieResponse response = GenieResponseBuilder.getSuccessResponse("", Profile.class);
        response.setResult(profile);
        return response;
    }

    /**
     * set current user
     *
     * @param uid - User id
     */
    @Override
    public GenieResponse<Void> setCurrentUser(String uid) {
        UserModel userModel = UserModel.findByUserId(mAppContext.getDBSession(), uid);
        if (userModel == null) {
            // TODO: 25/4/17 GEError Event has to be added here
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.INVALID_USER, ServiceConstants.NO_USER_WITH_SPECIFIED_ID, TAG, Void.class);
        }

        UserSessionModel session = UserSessionModel.findUserSession(mAppContext);
        boolean sessionCreationRequired = false;
        if (session == null) {
            sessionCreationRequired = true;
        } else if (!session.getUserSessionBean().getUid().equals(uid)) {
            session.endSession();
            //TODO GE_SESSION_END telemetry
            sessionCreationRequired = true;
        }

        if (sessionCreationRequired) {
            UserSessionModel userSessionModel = UserSessionModel.buildUserSession(mAppContext, uid);
            userSessionModel.startSession();
            //TODO GE_session-start telemetry to be sent
        }
        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
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
        return response;
    }

    @Override
    public GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessCriteria criteria) {
        String isContentIdentifier = null;
        if (!StringUtil.isNullOrEmpty(criteria.getContentIdentifier())) {
            isContentIdentifier = String.format(Locale.US, "%s = '%s'", ContentAccessEntry.COLUMN_NAME_CONTENT_IDENTIFIER, criteria.getContentIdentifier());
        }

        String isUid = null;
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

        String isContentType = String.format(Locale.US, "%s in ('%s')", ContentAccessEntry.COLUMN_NAME_CONTENT_TYPE, contentTypes);

        String filter = null;
        if (!StringUtil.isNullOrEmpty(isContentIdentifier) && !StringUtil.isNullOrEmpty(isUid)) {
            filter = String.format(Locale.US, " where (%s AND %s AND %s)", isContentIdentifier, isUid, isContentType);
        }
        if (!StringUtil.isNullOrEmpty(isContentIdentifier)) {
            filter = String.format(Locale.US, " where (%s AND %s)", isContentIdentifier, isContentType);
        }
        if (!StringUtil.isNullOrEmpty(isUid)) {
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
        return response;
    }

}