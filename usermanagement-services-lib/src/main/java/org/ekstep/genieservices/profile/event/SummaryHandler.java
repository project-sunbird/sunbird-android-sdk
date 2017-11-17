package org.ekstep.genieservices.profile.event;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.telemetry.TelemetryV3;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;

/**
 * Created by swayangjit on 9/5/17.
 */

public class SummaryHandler {

    private static final String TAG = SummaryHandler.class.getSimpleName();

    public static void handleTelemetryEvent(TelemetryV3 event, AppContext appContext) {
        if (event == null) {
            return;
        }
        if ("OE_ASSESS".equals(event.getEid()) && "ContentPlayer".equals(event.getContext().getEnv())) {
            processOEAssess(event, appContext);
        } else if ("OE_END".equals(event.getEid()) && "ContentPlayer".equals(event.getContext().getEnv())) {
            processOEEnd(event, appContext);
        }
    }

    private static void processOEAssess(TelemetryV3 event, AppContext appContext) {
        Logger.i(TAG, "Process OE ASSESS");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);
        summarizerService.saveLearnerAssessmentDetails(event);
    }

    private static void processOEEnd(TelemetryV3 event, AppContext appContext) {
        Logger.i(TAG, "Process OE END");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);
        summarizerService.saveLearnerContentSummaryDetails(event);
    }

}
