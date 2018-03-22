package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 3/22/2018.
 *
 * @author IndrajaMachani
 */
public class FlagDetailsAPI extends BaseAPI {

    private static final String TAG = FlagDetailsAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "flag";

    private Map<String, String> headers;

    public FlagDetailsAPI(AppContext appContext, Map<String, String> headers, String contentId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.CONTENT_BASE_URL), SERVICE_ENDPOINTS,
                        contentId),
                TAG);

        this.headers = headers;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return headers;
    }

    @Override
    protected String createRequestData() {
        return null;
    }
}
