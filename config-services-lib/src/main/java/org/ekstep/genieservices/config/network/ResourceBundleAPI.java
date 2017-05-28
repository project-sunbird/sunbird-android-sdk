package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 23/4/17.
 *
 * @author swayangjit
 */
public class ResourceBundleAPI extends BaseAPI {

    private static final String TAG = "service-RBAPI";
    private static final String ENDPOINT = "config/resourcebundles";
    private static final CharSequence SERVICE_ENDPOINTS = "config";


    public ResourceBundleAPI(AppContext appContext) {
        super(appContext, String.format(Locale.US, "%s/%s",
                String.format(Locale.US, appContext.getParams().getBaseApiUrl() + ServiceConstants.API.LP_EXTENSION, SERVICE_ENDPOINTS),
                ENDPOINT), TAG);
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
