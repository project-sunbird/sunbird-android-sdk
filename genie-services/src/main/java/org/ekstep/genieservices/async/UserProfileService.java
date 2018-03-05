package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IUserProfileService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TenantInfo;
import org.ekstep.genieservices.commons.bean.TenantInfoRequest;
import org.ekstep.genieservices.commons.bean.UserProfile;
import org.ekstep.genieservices.commons.bean.UserProfileDetailsRequest;

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
}
