package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 4/21/2017.
 *
 * @author anil
 */
public class OrdinalsAPI extends BaseAPI {

    private static final String TAG = OrdinalsAPI.class.getSimpleName();

    private static final String ENDPOINT = "ordinals/list";

    public OrdinalsAPI(AppContext appContext) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(ServiceConstants.Params.CONFIG_BASE_URL),
                        ENDPOINT),
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
