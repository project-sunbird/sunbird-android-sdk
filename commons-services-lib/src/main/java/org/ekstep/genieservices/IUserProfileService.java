package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Users profile.
 */
public interface IUserProfileService {

    GenieResponse<UserProfile> getUserProfileDetails(UserProfileDetailsRequest userProfileDetailsRequest);
}
