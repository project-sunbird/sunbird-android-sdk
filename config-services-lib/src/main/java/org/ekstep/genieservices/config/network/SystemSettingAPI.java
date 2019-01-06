package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 20/12/2018.
 *
 * @author anil
 */
public class SystemSettingAPI extends BaseAPI {

    private static final String TAG = SystemSettingAPI.class.getSimpleName();

    private static final String ENDPOINT = "system/settings/get";

    public SystemSettingAPI(AppContext appContext, String id) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s",
                        appContext.getParams().getString(IParams.Key.DATA_SERVICE_BASE_URL),
                        ENDPOINT, id),
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
