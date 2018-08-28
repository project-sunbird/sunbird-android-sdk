package org.ekstep.genieservices.notification.network;

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
public class AnnouncementDetailsAPI extends BaseAPI {

    private static final String TAG = AnnouncementDetailsAPI.class.getSimpleName();
    private static final CharSequence SERVICE_ENDPOINTS = "get";
    private Map<String, String> headers;

    public AnnouncementDetailsAPI(AppContext appContext, Map<String, String> headers, String announcementId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.ANNOUNCEMENT_BASE_URL),
                        SERVICE_ENDPOINTS,
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
