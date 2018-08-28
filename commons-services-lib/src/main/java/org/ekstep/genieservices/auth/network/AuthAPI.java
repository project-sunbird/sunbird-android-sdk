package org.ekstep.genieservices.auth.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 6/7/17.
 */

public class AuthAPI extends BaseAPI {
    private static final String TAG = AuthAPI.class.getSimpleName();
    private static final String VERSION = "1.0";
    private static final String ID = "ekstep.genie.device.register";

    private static final CharSequence SERVICE_ENDPOINTS = "credential/register";

    private String mobileAppConsumerBearerToken;
    private String mobileDeviceConsumerKey;

    public AuthAPI(AppContext appContext, String mobileDeviceConsumerKey, String mobileAppConsumerBearerToken) {
        super(appContext,
                String.format(Locale.US, "%s/consumer/%s/%s", appContext.getParams().getString(IParams.Key.APIGATEWAY_BASE_URL),
                        appContext.getParams().getString(IParams.Key.MOBILE_APP_CONSUMER), SERVICE_ENDPOINTS)
                , TAG);
        this.mobileDeviceConsumerKey = mobileDeviceConsumerKey;
        this.mobileAppConsumerBearerToken = mobileAppConsumerBearerToken;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Encoding", "gzip");
        //set the authentication header
        headerMap.put("Authorization", "Bearer " + mobileAppConsumerBearerToken);
        return headerMap;
    }

    @Override
    protected String createRequestData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("key", mobileDeviceConsumerKey);

        Map<String, Object> request = new HashMap<>();
        request.put("id", ID);
        request.put("ver", VERSION);
        request.put("ts", DateUtil.getCurrentTimestamp());
        request.put("request", requestMap);

        return GsonUtil.toJson(request);
    }

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }
}
