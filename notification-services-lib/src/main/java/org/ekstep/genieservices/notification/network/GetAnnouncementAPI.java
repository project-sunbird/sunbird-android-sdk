package org.ekstep.genieservices.notification.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 3/22/2018.
 *
 * @author IndrajaMachani
 */
public class GetAnnouncementAPI extends BaseAPI {

    private static final String TAG = GetAnnouncementAPI.class.getSimpleName();
    private static final CharSequence SERVICE_ENDPOINTS = "get";
    private Map<String, String> headers;

    public GetAnnouncementAPI(AppContext appContext, Map<String, String> headers, String announcementId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.ANNOUNCEMENT_BASE_URL), SERVICE_ENDPOINTS,
                        announcementId),
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
