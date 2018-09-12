package org.ekstep.genieservices.profile.event;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.telemetry.ProducerData;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;

/**
 * Created by swayangjit on 9/5/17.
 */

public class SummaryHandler {

    private static final String TAG = SummaryHandler.class.getSimpleName();

    // Data to be stored in these variable when START event is read
    private static String CONTENT_ID = null;
    private static String UID = null;

    public static void handleTelemetryEvent(Telemetry event, AppContext appContext) {
        if (event == null) {
            return;
        }

        if ("START".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEStart(event, appContext);
        } else if ("ASSESS".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEAssess(event, appContext);
        } else if ("END".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEEnd(event, appContext);
        }
    }

    private static void processOEStart(Telemetry event, AppContext appContext) {
        //Store contentId and uid
        UID = event.getActor().getId();
        CONTENT_ID = event.getObject().getId();

        Logger.i(TAG, "Process OE START");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);
        summarizerService.saveLearnerAssessmentDetails(event);
    }

    private static void processOEAssess(Telemetry event, AppContext appContext) {
        Logger.i(TAG, "Process OE ASSESS");
        SummarizerServiceImpl summarizerService = new SummarizerServiceImpl(appContext);

        //check if the details are present in CONTENT_ID and UID, and if any data is present related to these data,
        //then clear all the data belonging to them from LEARNER_ASSESSMENT_DETAILS and LEARNER_CONTENT_SUMMARY tables
        if (!StringUtil.isNullOrEmpty(UID) && !StringUtil.isNullOrEmpty(CONTENT_ID)
                && UID.equalsIgnoreCase(event.getActor().getId()) && CONTENT_ID.equalsIgnoreCase(event.getObject().getId())) {
            summarizerService.deletePreviousAssessmentDetails(UID, CONTENT_ID);

            CONTENT_ID = null;
            UID = null;
        }

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
