package org.ekstep.genieservices.profile.event;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.telemetry.ProducerData;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;

/**
 * Created by swayangjit on 9/5/17.
 */

public class SummaryHandler {

    private static final String TAG = SummaryHandler.class.getSimpleName();

    public static void handleTelemetryEvent(Telemetry event, AppContext appContext) {
        if (event == null) {
            return;
        }
        if ("ASSESS".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEAssess(event, appContext);
        } else if ("END".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEEnd(event, appContext);
        }
    }

    private static void processOEAssess(Telemetry event, AppContext appContext) {
        Logger.i(TAG, "Process OE ASSESS");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);
        summarizerService.saveLearnerAssessmentDetails(event);
    }

    private static void processOEEnd(Telemetry event, AppContext appContext) {
        Logger.i(TAG, "Process OE END");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);
        summarizerService.saveLearnerContentSummaryDetails(event);
    }

    private static boolean checkPdata(ProducerData pdata) {
        if (pdata != null && pdata.getPid() != null) {
            String pid = pdata.getPid();
            return pid.contains(ServiceConstants.Telemetry.CONTENT_PLAYER_PID);
        }
        return false;
    }

}
