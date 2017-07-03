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
public class TermsAPI extends BaseAPI {

    private static final String TAG = "service-TAPI";

    private static final String ENDPOINT = "terms/list";

    public TermsAPI(AppContext appContext) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(ServiceConstants.Params.LEARNING_PLATFORM_BASE_URL),
                        ENDPOINT),
                TAG);
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
