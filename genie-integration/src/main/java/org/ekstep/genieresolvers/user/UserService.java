package org.ekstep.genieresolvers.user;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Profile;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class UserService extends BaseService {
    private String appQualifier;
    private Context context;

    public UserService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void createUserProfile(Profile profile, IResponseHandler responseHandler) {
        CreateUserTask createUserTask = new CreateUserTask(context, appQualifier, profile);
        createAndExecuteTask(responseHandler, createUserTask);
    }

    public void deleteUser(String userId, IResponseHandler responseHandler) {
        DeleteUserTask deleteUserTask = new DeleteUserTask(context, appQualifier, userId);
        createAndExecuteTask(responseHandler, deleteUserTask);
    }

    public void updateUserProfile(Profile profile, IResponseHandler responseHandler) {
        UpdateUserTask updateUserTask = new UpdateUserTask(context, appQualifier, profile);
        createAndExecuteTask(responseHandler, updateUserTask);
    }

    public void getCurrentUser(IResponseHandler responseHandler) {
        GetCurrentUserTask getCurrentUserTask = new GetCurrentUserTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getCurrentUserTask);
    }

    public void getAllUserProfile(IResponseHandler responseHandler) {
        GetAllUsersTask getAllUsersTask = new GetAllUsersTask(context, appQualifier);
        createAndExecuteTask(responseHandler, getAllUsersTask);
    }

    public void setUser(String userId, IResponseHandler responseHandler) {
        SetCurrentUserTask setCurrentUserTask = new SetCurrentUserTask(context, appQualifier, userId);
        createAndExecuteTask(responseHandler, setCurrentUserTask);
    }
}
