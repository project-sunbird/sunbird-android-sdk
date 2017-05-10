package org.ekstep.genieservices.config.db.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 23/4/17.
 */

public class TermsAPI extends BaseAPI{

    private static final String TAG = "service-TAPI";
    private static final String ENDPOINT = "terms/list";
    private static final CharSequence SERVICE_ENDPOINTS = "learning";

    public TermsAPI(AppContext appContext) {
        super(appContext,String.format(Locale.US, "%s/%s", String.format(Locale.US, "%s/%s",
                appContext.getParams().getBaseApiUrl()
                        + ServiceConstants.API.LP_EXTENSION, SERVICE_ENDPOINTS), ENDPOINT), TAG);
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
