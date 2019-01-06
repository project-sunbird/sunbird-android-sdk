package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 18/12/2018.
 *
 * @author anil
 */
public class AcceptTermsAndConditionsAPI extends SunbirdBaseAPI {

    private static final String TAG = AcceptTermsAndConditionsAPI.class.getSimpleName();

    private static final String ENDPOINT = "tnc/accept";

    private Map<String, String> headers;
    private Map<String, Object> requestMap;

    public AcceptTermsAndConditionsAPI(AppContext appContext, Map<String, String> customHeaders, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.USER_SERVICE_BASE_URL),
                        ENDPOINT),
                TAG);

        this.headers = customHeaders;
        this.requestMap = requestMap;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return headers;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
