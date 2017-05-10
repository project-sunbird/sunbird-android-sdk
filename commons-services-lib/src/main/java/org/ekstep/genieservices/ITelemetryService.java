package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.BaseTelemetry;

/**
 * Created by swayangjit on 10/5/17.
 */

public interface ITelemetryService {

    GenieResponse<Void> saveTelemetry(String eventString);

    GenieResponse<Void> saveTelemetry(BaseTelemetry event);


}
