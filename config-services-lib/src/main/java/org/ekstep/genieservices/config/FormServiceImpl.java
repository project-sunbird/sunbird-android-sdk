package org.ekstep.genieservices.config;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IFormService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.FormRequest;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.config.network.FormReadAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public class FormServiceImpl extends BaseService implements IFormService {

    private static final String TAG = FormServiceImpl.class.getSimpleName();

    public FormServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Map<String, Object>> getForm(FormRequest formRequest) {
        String methodName = "getForm@ConfigServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("logLevel", "2");
        FormReadAPI formReadAPI = new FormReadAPI(mAppContext, formRequest);
        GenieResponse genieResponse = formReadAPI.post();
        if (genieResponse.getStatus()) {
            String body = genieResponse.getResult().toString();
            GenieResponse<Map<String, Object>> response;
            Map formData = null;

            if (body != null) {
                Map map = GsonUtil.fromJson(body, Map.class);
                Map result = (Map) map.get("result");
                Map form = (Map) result.get("form");

                if (form != null) {
                    formData = (Map) form.get("data");
                }
            }

            if (formData != null) {
                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                response.setResult(formData);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            } else {
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_FORM_DATA_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FORM, TAG);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FORM_DATA);
            }
            return response;
        }
        return genieResponse;
    }
}
