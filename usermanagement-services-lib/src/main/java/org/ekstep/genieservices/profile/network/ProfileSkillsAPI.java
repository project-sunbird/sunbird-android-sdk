package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 6/3/18.
 *
 * @author indraja
 */
public class ProfileSkillsAPI extends SunbirdBaseAPI {

    private static final String TAG = ProfileSkillsAPI.class.getSimpleName();

    private static final CharSequence SERVICE_ENDPOINTS = "skills";

    private Map<String, String> headers;

    public ProfileSkillsAPI(AppContext appContext, Map<String, String> customHeaders) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.DATA_SERVICE_BASE_URL),
                        SERVICE_ENDPOINTS),
                TAG);

        this.headers = customHeaders;
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
