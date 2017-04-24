package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.BuildConfigUtil;

import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 23/4/17.
 */

public class ResourceBundleAPI extends BaseAPI {

    private static final String TAG = "service-RBAPI";
    private static final String ENDPOINT = "config/resourcebundles";
    private static final CharSequence SERVICE_ENDPOINTS = "config";
    private static String URL;

    static {
        URL = String.format(Locale.US, BuildConfigUtil.TAXONOMY_BASE_URL, SERVICE_ENDPOINTS);
    }

    public ResourceBundleAPI(AppContext appContext) {
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
