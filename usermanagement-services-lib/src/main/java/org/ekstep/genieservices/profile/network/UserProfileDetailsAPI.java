package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 1/3/18.
 *
 * @author anil
 */

public class UserProfileDetailsAPI extends BaseAPI {

    private static final String TAG = UserProfileDetailsAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "read";

    private Map<String, String> headers;

    public UserProfileDetailsAPI(AppContext appContext, Map<String, String> customHeaders, String userId, String queryParams) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s?%s",
                        appContext.getParams().getString(IParams.Key.USER_SERVICE_BASE_URL), SERVICE_ENDPOINTS,
                        userId,
                        queryParams),
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
