package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IResponseHandler;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryService extends BaseService {

    public TelemetryService(AppContext appContext) {
        super(appContext);
    }

    public void saveTelemetry(String event, IResponseHandler responseHandler) {

    }
}
