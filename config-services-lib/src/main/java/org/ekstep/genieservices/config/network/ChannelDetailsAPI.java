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
public class ChannelDetailsAPI extends BaseAPI {

    private static final String TAG = ChannelDetailsAPI.class.getSimpleName();

    private static final String ENDPOINT = "read";

    private Map<String, String> headers;

    public ChannelDetailsAPI(AppContext appContext, Map<String, String> headers, String channelId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.CHANNEL_SERVICE_BASE_URL),
                        ENDPOINT, channelId),
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
