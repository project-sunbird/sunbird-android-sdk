package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.UserSession;

import java.util.List;

/**
 * Created on 10/5/17.
 *
 * @author mathew
 */
public interface IUserService {

    GenieResponse<Profile> createUserProfile(Profile profile);

    GenieResponse<Void> deleteUser(String uid);

    GenieResponse<Void> setCurrentUser(String uid);

    GenieResponse<Profile> getCurrentUser();

    GenieResponse<UserSession> getCurrentUserSession();

    GenieResponse<Profile> getAnonymousUser();

    GenieResponse<String> setAnonymousUser();

    GenieResponse<Profile> updateUserProfile(Profile profile);

    GenieResponse<List<ContentAccess>> getAllContentAccess(ContentAccessCriteria criteria);

}
