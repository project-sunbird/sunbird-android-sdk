package org.ekstep.genieresolvers.user;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;

import java.util.Map;

/**
 * This is the {@link UserService} class with all the required APIs to perform necessary operations related to Users
 */
public class UserService extends BaseService {
    private String appQualifier;
    private Context context;

    public UserService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     * This api is used to create a new user with specific {@link Profile}.
     * <p>
     * <p>On Successful creation of new profile, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to create a user, the response will have status as FALSE with the following error:
     * <p>FAILED - createProfile
     *
     * @param profile
     * @param responseHandler
     */
    public void createUserProfile(Profile profile, IResponseHandler<Map> responseHandler) {
        CreateUserTask createUserTask = new CreateUserTask(context, appQualifier, profile);
        createAndExecuteTask(responseHandler, createUserTask);
    }

    /**
     * This api is used to delete a existing user with a specific uid
     * <p>
     * <p>
     * <p>On successful deletion of a user, the response will return status as TRUE and with result set as Profile related data
     * <p>
     * <p>On failing to create new profile, the response will have status as FALSE with the following error:
     * <p>FAILED
     *
     * @param userId
     * @param responseHandler
     */
    public void deleteUser(String userId, IResponseHandler<Map> responseHandler) {
        DeleteUserTask deleteUserTask = new DeleteUserTask(context, appQualifier, userId);
        createAndExecuteTask(responseHandler, deleteUserTask);
    }

    /**
     * This api updates the specific profile that is passed to it.
     * <p>
     * <p>On successful updating the profile, the response will return status as TRUE and with the updated profile set in result.
     * <p>
     * <p>
     * <p>On failing to update the profile, the response will have status as FALSE with one of the following errors:
     * <p>INVALID_PROFILE
     * <p>VALIDATION_ERROR
     *
     * @param profile
     * @param responseHandler
     */
    public void updateUserProfile(Profile profile, IResponseHandler<Map> responseHandler) {
        UpdateUserTask updateUserTask = new UpdateUserTask(context, appQualifier, profile);
        createAndExecuteTask(responseHandler, updateUserTask);
    }

    /**
     * This api gets the current active user.
     * <p>
     * <p>On successful fetching the active user, the response will return status as TRUE and with the active profile set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would by default had set anonymous user as active.
     *
     * @param responseHandler
     */
    public void getCurrentUser(IResponseHandler<Map> responseHandler) {
        GetCurrentUserTask getCurrentUserTask = new GetCurrentUserTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getCurrentUserTask);
    }

    /**
     * This api returns the list of all user profiles. It does not include the anonymous user.
     *
     * @param responseHandler
     */
    public void getAllUserProfile(IResponseHandler<Map> responseHandler) {
        GetAllUsersTask getAllUsersTask = new GetAllUsersTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getAllUsersTask);
    }

    /**
     * This api sets the specific uid passed to it as active current user.
     * <p>
     * <p>
     * <p>On successful setting a user active, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to set the uid to current user, the response will have status as FALSE with the following error:
     * <p>INVALID_USER
     *
     * @param userId
     * @param responseHandler
     */
    public void setUser(String userId, IResponseHandler<Map> responseHandler) {
        SetCurrentUserTask setCurrentUserTask = new SetCurrentUserTask(context, appQualifier, userId);
        createAndExecuteTask(responseHandler, setCurrentUserTask);
    }
}
