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

    public ChannelDetailsAPI(AppContext appContext, String channelId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.CHANNEL_SERVICE_BASE_URL),
                        ENDPOINT, channelId),
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
