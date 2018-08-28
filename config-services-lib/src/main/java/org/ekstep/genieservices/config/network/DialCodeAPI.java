package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public class DialCodeAPI extends SunbirdBaseAPI {


    private static final String TAG = DialCodeAPI.class.getSimpleName();

    private static final String ENDPOINT = "read";

    private Map<String, Object> requestMap;

    public DialCodeAPI(AppContext appContext, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.DIALCODE_SERVICE_BASE_URL),
                        ENDPOINT),
                TAG);

        this.requestMap = requestMap;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
