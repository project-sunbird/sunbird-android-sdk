package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 6/3/18.
 * shriharsh
 */

public class SearchUserAPI extends BaseAPI {
    private static final String TAG = ProfileVisibilityAPI.class.getSimpleName();

    private static final String ENDPOINT = "search";

    private Map<String, Object> requestMap;
    private Map<String, String> headers;


    public SearchUserAPI(AppContext appContext, Map<String, String> customHeaders, Map<String, Object> requestMap) {
        super(appContext, String.format(Locale.US, "%s", ENDPOINT), TAG);

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
