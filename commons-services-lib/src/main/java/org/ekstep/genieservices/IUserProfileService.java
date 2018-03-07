package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.bean.UserProfileSkills;
import org.ekstep.genieservices.commons.bean.UserProfileSkillsRequest;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Users profile.
 */
public interface IUserProfileService {

    GenieResponse<UserProfile> getUserProfileDetails(UserProfileDetailsRequest userProfileDetailsRequest);

    GenieResponse<TenantInfo> getTenantInfo(TenantInfoRequest tenantInfoRequest);

    GenieResponse<UserProfileSkills> getUserProfileSkills(UserProfileSkillsRequest profileSkillsRequest);
}
