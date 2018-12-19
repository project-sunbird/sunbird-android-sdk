package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.AcceptTermsAndConditionsRequest;
import org.ekstep.genieservices.commons.bean.EndorseOrAddSkillRequest;
import org.ekstep.genieservices.commons.bean.FileUploadResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UpdateUserInfoRequest;
import org.ekstep.genieservices.commons.bean.UploadFileRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;
import org.ekstep.genieservices.commons.bean.UserProfileSkill;
import org.ekstep.genieservices.commons.bean.UserProfileSkillsRequest;
import org.ekstep.genieservices.commons.bean.UserSearchCriteria;
import org.ekstep.genieservices.commons.bean.UserSearchResult;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Users profile.
 */
public interface IUserProfileService {

    GenieResponse<UserProfile> getUserProfileDetails(UserProfileDetailsRequest userProfileDetailsRequest);

    GenieResponse<TenantInfo> getTenantInfo(TenantInfoRequest tenantInfoRequest);

    GenieResponse<UserSearchResult> searchUser(UserSearchCriteria profileVisibilityRequest);

    GenieResponse<UserProfileSkill> getSkills(UserProfileSkillsRequest profileSkillsRequest);

    GenieResponse<Void> endorseOrAddSkill(EndorseOrAddSkillRequest endorseOrAddSkillRequest);

    GenieResponse<Void> setProfileVisibility(ProfileVisibilityRequest profileVisibilityRequest);

    GenieResponse<FileUploadResult> uploadFile(UploadFileRequest uploadFileRequest);

    GenieResponse<Void> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest);

    GenieResponse<Void> acceptTermsAndConditions(AcceptTermsAndConditionsRequest acceptTermsAndConditionsRequest);

}
