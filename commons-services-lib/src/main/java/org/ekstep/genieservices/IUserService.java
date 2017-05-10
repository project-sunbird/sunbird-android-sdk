package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;

/**
 * Created by mathew on 10/5/17.
 */

public interface IUserService {

    public GenieResponse<Profile> createUserProfile(Profile profile);
    public GenieResponse<Void> deleteUser(String uid);
    public GenieResponse<Void> setCurrentUser(String uid);
    public GenieResponse<Profile> getCurrentUser();
    public GenieResponse<Profile> getAnonymousUser();
    public GenieResponse<String> setAnonymousUser();
    public GenieResponse<Profile> updateUserProfile(Profile profile);
}
