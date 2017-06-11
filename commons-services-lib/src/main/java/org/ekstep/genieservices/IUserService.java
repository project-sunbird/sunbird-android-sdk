package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentAccessLearnerState;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Users
 */
public interface IUserService {

    /**
     * This api is used to create a new user with specific {@link Profile}.
     * <p>
     * <p>On Successful creation of new profile, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to delete a user, the response will have status as FALSE with the following error:
     * <p>FAILED - createProfile
     *
     * @param profile - {@link Profile}
     * @return {@link GenieResponse<Profile>}
     */
    GenieResponse<Profile> createUserProfile(Profile profile);

    /**
     * @return
     */
    GenieResponse<List<Profile>> getAllUserProfile();

    /**
     * This api is used to delete a existing user with a specific uid
     * <p>
     * <p>
     * <p>On successful deletion of a user, the response will return status as TRUE and with result set as Profile related data
     * <p>
     * <p>On failing to create new profile, the response will have status as FALSE with the following error:
     * <p>FAILED
     *
     * @param uid - {@link String}
     * @return {@link String}
     */
    GenieResponse<Void> deleteUser(String uid);

    /**
     * This api sets the specific uid passed to it as active current user.
     * <p>
     * <p>
     * <p>On successful setting a user active, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to set the uid to current user, the response will have status as FALSE with the following error:
     * <p>INVALID_USER
     *
     * @param uid - {@link String}
     * @return {@link String}
     */
    public GenieResponse<Void> setCurrentUser(String uid);

    /**
     * This api gets the current active user.
     * <p>
     * <p>On successful fetching the active user, the response will return status as TRUE and with the active profile set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would by default had set anonymous user as active.
     *
     * @return {@link GenieResponse<Profile>}
     */
    public GenieResponse<Profile> getCurrentUser();

    /**
     * This api gets the current active user session.
     * <p>
     * <p>On successful fetching the active user session, the response will return status as TRUE and with the active user session set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would by default had set anonymous user session as active.
     *
     * @return {@link GenieResponse<UserSession>}
     */
    public GenieResponse<UserSession> getCurrentUserSession();

    /**
     * This api gets the anonymous user the one if exists or a new anonymous user will be created.
     * <p>
     * <p>On successful fetching the anonymous user, the response will return status as TRUE and with the profile of anonymous user set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would get anonymous user if exists or a new one will be created.
     *
     * @return {@link GenieResponse<Profile>}
     */
    public GenieResponse<Profile> getAnonymousUser();

    /**
     * This api gets the anonymous user from getAnonymousUser() api and sets it to current active user.
     * <p>
     * <p>On successful setting the anonymous user as active, the response will return status as TRUE and with the uid of anonymous user set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would get anonymous user if exists or a new one will be created and set to active user.
     *
     * @return {@link GenieResponse<String>}
     */
    public GenieResponse<String> setAnonymousUser();

    /**
     * This api updates the specific profile that is possed to it.
     * <p>
     * <p>On successful updating the profile, the response will return status as TRUE and with the updated profile set in result.
     * <p>
     * <p>
     * <p>On failing to update the profile, the response will have status as FALSE with one of the following errors:
     * <p>INVALID_PROFILE
     * <p>VALIDATION_ERROR
     *
     * @param profile
     * @return
     */
    public GenieResponse<Profile> updateUserProfile(Profile profile);

    /**
     * This api gives the status each content of being accessed.
     * <p>
     * <p>Response status always be True, with all the contents access state set in result
     *
     * @param criteria - {@link ContentAccessFilterCriteria}
     * @return {@link GenieResponse<List<ContentAccess>>}
     */
    GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessFilterCriteria criteria);

    /**
     * This api is used to the set the state of learner.
     * <p>
     * <p> On successful setting the learner state, the response will return status as TRUE
     * <p>
     * <p> On failing to set the learner state, the response will have status as FALSE with the following error:
     * <p>PROFILE_NOT_FOUND
     *
     * @param contentAccessLearnerState
     * @return
     */
    GenieResponse<Void> setLearnerState(ContentAccessLearnerState contentAccessLearnerState);

    /**
     * This API is used to import profiles.
     *
     * @param dbSession
     * @param metadata
     * @return
     */
    GenieResponse<Void> importProfile(IDBSession dbSession, Map<String, Object> metadata);

    /**
     * This API is used to export profiles.
     *
     * @param userIds
     * @return
     */
    GenieResponse<Void> exportProfile(List<String> userIds, File destinationFolder, String sourceDBFilePath, String destinationDBFilePath, IDBSession destinationDB, Map<String, Object> metadata);

}
