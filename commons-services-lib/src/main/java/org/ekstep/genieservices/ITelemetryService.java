package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

/**
 * Created by swayangjit on 10/5/17.
 */

public interface ITelemetryService {

    GenieResponse<Void> saveTelemetry(String eventString);

    GenieResponse<Void> saveTelemetry(Telemetry event);


}
