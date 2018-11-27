package org.ekstep.genieservices.telemetry.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 20/11/18.
 *
 * @author swayangjit
 */
public class DeviceRegisterAPI extends BaseAPI {

    private static final String TAG = DeviceRegisterAPI.class.getSimpleName();
    private static final CharSequence ENDPOINTS = "register";

    private Map<String, Object> mRequestMap;

    public DeviceRegisterAPI(AppContext appContext, Map<String, Object> requestMap, String deviceId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.DEVICE_REGISTER_BASE_URL), ENDPOINTS,
                        deviceId),
                TAG);
        mRequestMap = requestMap;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", mRequestMap);
        return GsonUtil.toJson(request);
    }
}
