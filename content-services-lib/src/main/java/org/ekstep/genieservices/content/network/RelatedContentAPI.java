package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/18/2017.
 *
 * @author anil
 */
public class RelatedContentAPI extends BaseAPI {

    private static final String TAG = RelatedContentAPI.class.getSimpleName();

    private static final String ENDPOINT = "recommend";

    private static final String VERSION = "1.0";
    private static final String ID = "ekstep.analytics.recommendations";

    private Map<String, Object> requestMap;

    public RelatedContentAPI(AppContext appContext, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/%s", appContext.getParams().getString(IParams.Key.CONTENT_BASE_URL), ENDPOINT),
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
        request.put("id", ID);
        request.put("ver", VERSION);
        request.put("ts", DateUtil.getCurrentTimestamp());
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
