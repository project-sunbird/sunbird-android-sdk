package org.ekstep.genieservices.page.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.PageAssembleCriteria;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageAPI extends BaseAPI {
    private static final String TAG = PageAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "page/assemble";

    private PageAssembleCriteria criteria;
    private Map<String, String> customHeaders;

    public PageAPI(AppContext appContext, Map<String, String> customHeaders, PageAssembleCriteria criteria) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.PAGE_SERVICE_BASE_URL),
                        SERVICE_ENDPOINTS),
                TAG);
        this.criteria = criteria;
        this.customHeaders = customHeaders;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return this.customHeaders;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", criteria);
        return GsonUtil.toJson(request);
    }
}
