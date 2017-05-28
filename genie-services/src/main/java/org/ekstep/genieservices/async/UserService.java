package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;

public class UserService {
    private IUserService userService;

    public UserService(GenieService genieService) {
        this.userService = genieService.getUserProfileService();
    }

    public void createUserProfile(final Profile profile, IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.createUserProfile(profile);
            }
        });
    }

    public void deleteUser(final String uid, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userService.deleteUser(uid);
            }
        });
    }

    public void setCurrentUser(final String uid, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userService.setCurrentUser(uid);
            }
        });
    }

    public void getCurrentUser(IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.getCurrentUser();
            }
        });
    }

    public void getCurrentUserSession(IResponseHandler<UserSession> responseHandler) {
        new AsyncHandler<UserSession>(responseHandler).execute(new IPerformable<UserSession>() {
            @Override
            public GenieResponse<UserSession> perform() {
                return userService.getCurrentUserSession();
            }
        });
    }

    public void getAnonymousUser(IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.getAnonymousUser();
            }
        });
    }

    public void setAnonymousUser(IResponseHandler<String> responseHandler) {
        new AsyncHandler<String>(responseHandler).execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return userService.setAnonymousUser();
            }
        });
    }

    public void updateUserProfile(final Profile profile, IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.updateUserProfile(profile);
            }
        });
    }

}
