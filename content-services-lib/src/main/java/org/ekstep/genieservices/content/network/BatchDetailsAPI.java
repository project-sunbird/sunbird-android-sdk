package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 24/5/18.
 *
 * @author anil
 */
public class BatchDetailsAPI extends SunbirdBaseAPI {

    private static final String TAG = BatchDetailsAPI.class.getSimpleName();

    private static final String SERVICE_ENDPOINTS = "batch/read";

    private Map<String, String> headers;

    public BatchDetailsAPI(AppContext appContext, Map<String, String> customHeaders, String batchId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.COURSE_SERVICE_BASE_URL),
                        SERVICE_ENDPOINTS,
                        batchId),
                TAG);

        this.headers = customHeaders;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return this.headers;
    }

    @Override
    protected String createRequestData() {
        return null;
    }
}
