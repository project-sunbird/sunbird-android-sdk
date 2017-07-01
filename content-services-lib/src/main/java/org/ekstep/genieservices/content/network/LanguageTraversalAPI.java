package org.ekstep.genieservices.content.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.BaseAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created on 5/22/2017.
 *
 * @author anil
 */
public class LanguageTraversalAPI extends BaseAPI {

    private static final String TAG = LanguageTraversalAPI.class.getSimpleName();

    private static final String ENDPOINT = "language/traversals";

    public LanguageTraversalAPI(AppContext appContext, String languageId) {
        super(appContext,
                String.format(Locale.US, "%s/%s/%s", appContext.getParams().getString(ServiceConstants.Params.LANGUAGE_PLATFORM_BASE_URL), ENDPOINT, languageId),
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
