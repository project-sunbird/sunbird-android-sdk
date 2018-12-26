package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/15/2017.
 *
 * @author anil
 */
public class ContentSearchAPI extends BaseAPI {

    private static final String TAG = ContentSearchAPI.class.getSimpleName();

    private static final String ENDPOINT = "search";

    private Map<String, Object> requestMap;

    public ContentSearchAPI(AppContext appContext, Map<String, Object> requestMap) {

        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.SEARCH_BASE_URL),
                        ENDPOINT),
                TAG);

        this.requestMap = requestMap;
    }

    public ContentSearchAPI(AppContext appContext, Map<String, Object> requestMap, String framework, String langCode) {

        super(appContext,
                String.format(Locale.US, "%s/%s%s",
                        appContext.getParams().getString(IParams.Key.SEARCH_BASE_URL),
                        ENDPOINT, "?framework=" + framework + "&lang=" + langCode),
                TAG);

        this.requestMap = requestMap;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("user-id", "123");
        return headers;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }

}
