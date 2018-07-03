package org.ekstep.genieservices.config;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IDialCodeService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.DialCodeRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.config.network.DialCodeAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 02/7/18.
 */
public class DialCodeServiceImpl extends BaseService implements IDialCodeService {

    private static final String TAG = DialCodeServiceImpl.class.getSimpleName();

    public DialCodeServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Map<String, Object>> getDialCode(DialCodeRequest dialCodeRequest) {
        String methodName = "getDialCode@ConfigServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");

        GenieResponse<Map<String, Object>> response;
        DialCodeAPI dialCodeGetAPI = new DialCodeAPI(mAppContext, getDialCodeRequest(dialCodeRequest));
        GenieResponse apiResponse = dialCodeGetAPI.post();
        if (apiResponse.getStatus()) {
            String body = apiResponse.getResult().toString();
            Map<String, Object> map = GsonUtil.fromJson(body, HashMap.class);
            Map<String, Object> result = (Map<String, Object>) map.get("result");
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(result);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        }

        response = GenieResponseBuilder.getErrorResponse(apiResponse.getError(), (String) apiResponse.getErrorMessages().get(0), TAG);
        TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, (String) apiResponse.getErrorMessages().get(0));
        return response;
    }

    private Map<String, Object> getDialCodeRequest(DialCodeRequest dialCodeRequest) {
        Map<String, Object> dialcodeMap = new HashMap<>();
        dialcodeMap.put("identifier", dialCodeRequest.getIdentifier());

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dialcode", dialcodeMap);
        return requestMap;
    }

}
