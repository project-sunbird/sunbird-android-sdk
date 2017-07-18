package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 18/7/17.
 */
public class AuthServiceImpl extends BaseService implements IAuthService {

    private static final String TAG = AuthServiceImpl.class.getSimpleName();

    public AuthServiceImpl(AppContext appContext) {
        super(appContext);
        this.mAppContext = appContext;
    }

    @Override
    public GenieResponse<String> getMobileDeviceBearerToken() {
        String mobileDeviceConsumerBearerToken = AuthHandler.generateMobileDeviceConsumerBearerToken(mAppContext);
        if (!StringUtil.isNullOrEmpty(mobileDeviceConsumerBearerToken)) {
            GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse("", String.class);
            response.setResult(mobileDeviceConsumerBearerToken);
            return response;
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.TOKEN_GENERATION_FAILED, ServiceConstants.ErrorMessage.FAILED_TO_GENERATE_TOKEN, TAG, String.class);
        }
    }

}
