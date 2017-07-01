package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/12/2017.
 *
 * @author anil
 */
public class ContentDetailsAPI extends BaseAPI {

    private static final String TAG = ContentDetailsAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "learning";

    public ContentDetailsAPI(AppContext appContext, String contentId) {
        super(appContext, String.format(Locale.US, "%s/content/%s",
                String.format(Locale.US, appContext.getParams().getString(ServiceConstants.Params.API_BASE_URL) + ServiceConstants.API.LP_EXTENSION, SERVICE_ENDPOINTS),
                contentId), TAG);
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
