package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 5/22/2017.
 *
 * @author anil
 */
public class ContentListingAPI extends BaseAPI {

    private static final String TAG = ContentListingAPI.class.getSimpleName();

    private static final String VERSION = "1.0";
    private String pageIdentifier;
    private Map<String, Object> requestMap;

    public ContentListingAPI(AppContext appContext, String pageIdentifier, Map<String, Object> requestMap) {
        super(appContext,
                String.format(Locale.US, "%s/pageApi/assemble/%s", appContext.getParams().getString(IParams.Key.CONTENT_LISTING_BASE_URL), pageIdentifier),
                TAG);

        this.pageIdentifier = pageIdentifier;
        this.requestMap = requestMap;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("id", pageIdentifier);
        request.put("ver", VERSION);
        request.put("ets", DateUtil.getEpochTime());
        request.put("params", new HashMap<>().put("msgid", UUID.randomUUID()));
        request.put("request", requestMap);
        return GsonUtil.toJson(request);
    }
}
