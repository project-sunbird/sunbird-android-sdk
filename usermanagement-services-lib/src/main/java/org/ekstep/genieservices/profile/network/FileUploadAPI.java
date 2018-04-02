package org.ekstep.genieservices.profile.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.BaseAPI;
import org.ekstep.genieservices.commons.network.FormRequestBody;
import org.ekstep.genieservices.commons.network.IRequestBody;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 7/3/18.
 * shriharsh
 */

public class FileUploadAPI extends BaseAPI {
    private static final String TAG = ProfileVisibilityAPI.class.getSimpleName();

    private static final String ENDPOINT = "media/upload";

    private Map<String, String> requestMap;
    private Map<String, String> headers;


    public FileUploadAPI(AppContext appContext, Map<String, String> customHeaders, Map<String, String> requestMap) {
        super(appContext, String.format(Locale.US, "%s/%s",
                appContext.getParams().getString(IParams.Key.CONTENT_BASE_URL),
                ENDPOINT), TAG);

        this.requestMap = requestMap;
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

    protected IRequestBody getRequestBody() {
        IRequestBody requestBody = new FormRequestBody();
        requestBody.setBody(requestMap);
        return requestBody;
    }
}
