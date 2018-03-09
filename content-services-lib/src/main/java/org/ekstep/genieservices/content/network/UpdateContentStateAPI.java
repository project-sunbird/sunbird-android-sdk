package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class UpdateContentStateAPI extends BaseAPI {

    private static final String TAG = UpdateContentStateAPI.class.getSimpleName();

    private static final String ENDPOINT = "content/state/update";

    private Map<String, String> headers;
    private Map<String, Object> requestMap;

    public UpdateContentStateAPI(AppContext appContext, Map<String, String> customHeaders, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.COURSE_SERVICE_BASE_URL),
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
