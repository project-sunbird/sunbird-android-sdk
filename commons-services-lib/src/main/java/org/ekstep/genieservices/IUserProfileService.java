package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.FileUploadResult;
import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.SearchUserRequest;
import org.ekstep.genieservices.commons.bean.SearchUserResult;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UploadFileRequest;
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

    GenieResponse<Void> setProfileVisibility(ProfileVisibilityRequest profileVisibilityRequest);

    GenieResponse<SearchUserResult> searchUser(SearchUserRequest profileVisibilityRequest);

    GenieResponse<UserProfileSkills> getUserProfileSkills(UserProfileSkillsRequest profileSkillsRequest);

    GenieResponse<Void> endorseOrAddSkill(EndorseOrAddSkillRequest endorseOrAddSkillRequest);

    GenieResponse<FileUploadResult> uploadFile(UploadFileRequest uploadFileRequest);

}
