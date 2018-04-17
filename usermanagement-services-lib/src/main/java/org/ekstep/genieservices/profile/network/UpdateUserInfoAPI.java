package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 3/23/18.
 *
 * @author indraja
 */

public class UpdateUserInfoAPI extends SunbirdBaseAPI {
    private static final String TAG = UpdateUserInfoAPI.class.getSimpleName();

    private static final String ENDPOINT = "update";

    private Map<String, Object> requestMap;
    private Map<String, String> headers;


    public UpdateUserInfoAPI(AppContext appContext, Map<String, String> customHeaders, Map<String, Object> requestMap) {
        super(appContext, String.format(Locale.US, "%s/%s",
                appContext.getParams().getString(IParams.Key.USER_SERVICE_BASE_URL),
                ENDPOINT), TAG);

        this.requestMap = requestMap;
        this.headers = customHeaders;

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
