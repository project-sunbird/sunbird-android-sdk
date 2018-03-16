package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 15/03/2018.
 *
 * @author Indraja Machani
 */
public class FrameworkDetailsApi extends BaseAPI {

    private static final String TAG = FrameworkDetailsApi.class.getSimpleName();

    private static final String ENDPOINT = "read/NCF";

    private Map<String, String> headers;

    public FrameworkDetailsApi(AppContext appContext, Map<String, String> headers) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.FRAMEWORK_SERVICE_BASE_URL),
                        ENDPOINT),
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
