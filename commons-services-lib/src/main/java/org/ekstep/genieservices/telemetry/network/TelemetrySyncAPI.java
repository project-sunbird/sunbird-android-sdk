package org.ekstep.genieservices.telemetry.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 8/5/17.
 *
 * @author swayangjit
 */
public class TelemetrySyncAPI extends BaseAPI {

    private static final String TAG = TelemetrySyncAPI.class.getSimpleName();
    private static final CharSequence SERVICE_ENDPOINTS = "telemetry";

    private byte[] mData;

    public TelemetrySyncAPI(AppContext appContext, byte[] data) {
        super(appContext, String.format(Locale.US, "%s/telemetry",
                String.format(Locale.US, appContext.getParams().getBaseApiUrl() + ServiceConstants.API.EP_EXTENSION, SERVICE_ENDPOINTS)),
                TAG);
        mData = data;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Encoding", "gzip");
        return headerMap;
    }

    @Override
    protected byte[] getRequestData() {
        return mData;
    }

    @Override
    protected String createRequestData() {
        return null;
    }
}
