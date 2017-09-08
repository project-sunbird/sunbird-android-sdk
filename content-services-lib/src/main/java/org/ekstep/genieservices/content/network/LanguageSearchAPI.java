package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/24/2017.
 *
 * @author anil
 */
public class LanguageSearchAPI extends BaseAPI {

    private static final String TAG = LanguageSearchAPI.class.getSimpleName();

    private static final String ENDPOINT = "search";

    private String requestData;

    public LanguageSearchAPI(AppContext appContext, String requestData) {
        super(appContext,
                String.format(Locale.US, "%s/%s", appContext.getParams().getString(IParams.Key.LANGUAGE_PLATFORM_BASE_URL), ENDPOINT),
                TAG);

        this.requestData = requestData;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> requestMap = GsonUtil.fromJson(requestData, Map.class);

        Map<String, Object> request = new HashMap<>();
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
