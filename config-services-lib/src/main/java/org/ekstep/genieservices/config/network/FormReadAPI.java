package org.ekstep.genieservices.config.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.FormRequest;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.SunbirdBaseAPI;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public class FormReadAPI extends SunbirdBaseAPI {


    private static final String TAG = FormReadAPI.class.getSimpleName();

    private static final String ENDPOINT = "form/read";

    private FormRequest formRequest;

    public FormReadAPI(AppContext appContext, FormRequest formRequest) {
        super(appContext,
                String.format(Locale.US, "%s/%s",
                        appContext.getParams().getString(IParams.Key.FORM_SERVICE_BASE_URL),
                        ENDPOINT),
                TAG);

        this.formRequest = formRequest;
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected String createRequestData() {
        Map<String, Object> request = new HashMap<>();
        request.put("request", formRequest);
        return GsonUtil.toJson(request);
    }
}
