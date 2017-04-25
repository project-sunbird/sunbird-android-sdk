package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.BuildConfigUtil;

import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 23/4/17.
 */

public class TermsAPI extends BaseAPI{

    private static final String TAG = "service-TAPI";
    private static final String ENDPOINT = "terms/list";
    private static final CharSequence SERVICE_ENDPOINTS = "learning";
    private static String URL;

    static {
        URL = String.format(Locale.US, BuildConfigUtil.TAXONOMY_BASE_URL, SERVICE_ENDPOINTS);
    }

    public TermsAPI(AppContext appContext) {
        super(appContext, String.format(Locale.US, "%s/%s", URL, ENDPOINT), TAG);
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        return null;
    }
}
