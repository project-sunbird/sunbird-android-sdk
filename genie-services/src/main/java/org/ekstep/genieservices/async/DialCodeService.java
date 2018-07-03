package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IDialCodeService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.DialCodeRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created by swayangjit on 02/7/18.
 */
public class DialCodeService {

    private IDialCodeService dialCodeService;

    public DialCodeService(GenieService genieService) {
        this.dialCodeService = genieService.getDialCodeService();
    }

    /**
     * This api is used to get Dialcode
     *
     * @param dialCodeRequest - {@link DialCodeRequest}
     * @param responseHandler - {@link IResponseHandler <Map<String,Object>>}
     */
    public void getDialCode(final DialCodeRequest dialCodeRequest, IResponseHandler<Map<String, Object>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Map<String, Object>>() {
            @Override
            public GenieResponse<Map<String, Object>> perform() {
                return dialCodeService.getDialCode(dialCodeRequest);
            }
        }, responseHandler);
    }

}
