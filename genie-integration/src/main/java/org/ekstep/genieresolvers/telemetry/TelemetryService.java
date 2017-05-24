package org.ekstep.genieresolvers.telemetry;

import android.content.Context;

import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.TaskHandler;
import org.ekstep.genieservices.commons.IResponseHandler;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class TelemetryService {
    private String appQualifier;
    private Context context;

    public TelemetryService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void saveTelemetryEvent(String eventString, IResponseHandler responseHandler) {
        TelemetryEventTask telemetryEventTask = new TelemetryEventTask(context, appQualifier, eventString);

        createAndExecuteTask(responseHandler, telemetryEventTask);
    }

    private void createAndExecuteTask(IResponseHandler responseHandler, BaseTask task) {
        new TaskHandler(responseHandler).execute(task);
    }

}
