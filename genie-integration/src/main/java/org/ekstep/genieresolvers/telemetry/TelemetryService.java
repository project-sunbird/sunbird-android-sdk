package org.ekstep.genieresolvers.telemetry;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;

/**
 *
 * This is the {@link TelemetryService} with all the required APIs to perform necessary operations related to Telemetry
 */

public class TelemetryService extends BaseService {
    private String appQualifier;
    private Context context;

    public TelemetryService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     *
     * This api will save the telemetry details passed to it as String.
     * <p>
     * <p> On successful saving the telemetry, the response will return status as TRUE and with "Event Saved Successfully" message.
     * <p>
     * <p>On failing to save the telemetry details, the response will return status as FALSE and the error be the following:
     * <p>PROCESSING_ERROR
     *
     * @param eventString
     * @param responseHandler
     */
    public void saveTelemetryEvent(String eventString, IResponseHandler responseHandler) {
        TelemetryEventTask telemetryEventTask = new TelemetryEventTask(context, appQualifier, eventString);
        createAndExecuteTask(responseHandler, telemetryEventTask);
    }

}
