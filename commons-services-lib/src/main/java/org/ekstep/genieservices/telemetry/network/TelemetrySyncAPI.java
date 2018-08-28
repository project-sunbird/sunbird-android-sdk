package org.ekstep.genieservices.telemetry.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
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
    private static final CharSequence ENDPOINTS = "telemetry";

    private byte[] mData;

    public TelemetrySyncAPI(AppContext appContext, byte[] data) {
        super(appContext,
                String.format(Locale.US, "%s/%s", appContext.getParams().getString(IParams.Key.TELEMETRY_BASE_URL), ENDPOINTS),
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
