package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IFormService;
import org.ekstep.genieservices.commons.FormRequest;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public class FormService {

    private IFormService formService;

    public FormService(GenieService genieService) {
        this.formService = genieService.getFormService();
    }

    /**
     * This api is used to get Form
     *
     * @param formRequest     - {@link FormRequest}
     * @param responseHandler - {@link IResponseHandler <Map<String,Object>>}
     */
    public void getForm(final FormRequest formRequest, IResponseHandler<Map<String, Object>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return formService.getForm(formRequest);
            }
        }, responseHandler);
    }

}
