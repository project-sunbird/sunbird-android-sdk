package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.commons.IResponseHandler;
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
 * This class provides APIs for performing {@link UserProfileService} related operations on a separate thread.
 */
public class UserProfileService {

    private IUserProfileService userProfileService;

    public UserProfileService(GenieService genieService) {
        this.userProfileService = genieService.getUserProfileService();
    }

    /**
     * This api is used to get the user profile details.
     *
     * @param userProfileDetailsRequest - {@link UserProfileDetailsRequest}
     * @param responseHandler           - {@link IResponseHandler<UserProfile>}
     */
    public void getUserProfileDetails(final UserProfileDetailsRequest userProfileDetailsRequest, IResponseHandler<UserProfile> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<UserProfile>() {
            @Override
            public GenieResponse<UserProfile> perform() {
                return userProfileService.getUserProfileDetails(userProfileDetailsRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get the tenant info.
     *
     * @param tenantInfoRequest - {@link TenantInfoRequest}
     * @param responseHandler   - {@link IResponseHandler<TenantInfo>}
     */
    public void getTenantInfo(final TenantInfoRequest tenantInfoRequest, IResponseHandler<TenantInfo> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<TenantInfo>() {
            @Override
            public GenieResponse<TenantInfo> perform() {
                return userProfileService.getTenantInfo(tenantInfoRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to set the visibility details of the field related to profile
     *
     * @param profileVisibilityRequest - {@link ProfileVisibilityRequest}
     * @param responseHandler          - {@link IResponseHandler<TenantInfo>}
     */
    public void setProfileVisibility(final ProfileVisibilityRequest profileVisibilityRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userProfileService.setProfileVisibility(profileVisibilityRequest);
            }
        }, responseHandler);
    }

    /*
     * This api is used search user
     *
             * @param userSearchCriteria - {@link UserSearchCriteria}
     * @param responseHandler   - {@link IResponseHandler<TenantInfo>}
     */
    public void searchUser(final UserSearchCriteria userSearchCriteria, IResponseHandler<UserSearchResult> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<UserSearchResult>() {
            @Override
            public GenieResponse<UserSearchResult> perform() {
                return userProfileService.searchUser(userSearchCriteria);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get the user profile skills
     *
     * @param profileSkillsRequest - {@link UserProfileSkillsRequest}
     * @param responseHandler      - {@link IResponseHandler< UserProfileSkill >}
     */
    public void getSkills(final UserProfileSkillsRequest profileSkillsRequest, IResponseHandler<UserProfileSkill> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<UserProfileSkill>() {
            @Override
            public GenieResponse<UserProfileSkill> perform() {
                return userProfileService.getSkills(profileSkillsRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to endorse or add skill
     *
     * @param endorseOrAddSkillRequest - {@link EndorseOrAddSkillRequest}
     * @param responseHandler          - {@link IResponseHandler< UserProfileSkill >}
     */
    public void endorseOrAddSkill(final EndorseOrAddSkillRequest endorseOrAddSkillRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userProfileService.endorseOrAddSkill(endorseOrAddSkillRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to upload file
     *
     * @param uploadFileRequest
     * @param responseHandler
     */
    public void uploadFile(final UploadFileRequest uploadFileRequest, IResponseHandler<FileUploadResult> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<FileUploadResult>() {
            @Override
            public GenieResponse<FileUploadResult> perform() {
                return userProfileService.uploadFile(uploadFileRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to update user info
     *
     * @param updateUserInfoRequest
     * @param responseHandler
     */
    public void updateUserInfo(final UpdateUserInfoRequest updateUserInfoRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userProfileService.updateUserInfo(updateUserInfoRequest);
            }
        }, responseHandler);
    }
}
