package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 3/22/2018.
 *
 * @author IndrajaMachani
 */
public class FlagContentAPI extends BaseAPI {

    private static final String TAG = FlagContentAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "flag";

    private Map<String, String> headers;
    private Map<String, Object> requestMap;

    public FlagContentAPI(AppContext appContext, Map<String, String> headers, String contentId, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.CONTENT_BASE_URL), SERVICE_ENDPOINTS,
                        contentId),
                TAG);

        this.headers = headers;
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
