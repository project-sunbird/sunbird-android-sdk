package org.ekstep.genieservices.telemetry.event;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.Logger;

/**
 * Created by swayangjit on 9/5/17.
 */

public class TelemetryHandler {

    private static final String TAG = TelemetryHandler.class.getSimpleName();

    public static void handleTelemetryEvent(Telemetry event, AppContext context) {
        if (event == null) {
            return;
        }
        if ("OE_ASSESS".equals(event.getEid())) {
            processOEAssess(event, context.getDBSession());
        } else if ("OE_END".equals(event.getEid())) {
            processOEEnd(event,  context.getDBSession());
        } else if ("GE_FEEDBACK".equals(event.getEid())) {
            processGEFeedbackEvent(event,  context.getDBSession());
        } else if ("GE_LAUNCH_GAME".equals(event.getEid())) {
            processGELaunchGameEvent(event,  context.getDBSession());
        } else if ("OE_INTERACT".equals(event.getEid())) {
            processOEInteractEvent(event,  context.getDBSession());
        }
    }

    private static void processOEAssess(Telemetry event, IDBSession dbSession) {
        Logger.i(TAG, "Process OE ASSESS");
//        LearnerAssessments assessmentsReader = new LearnerAssessments(event);
//        assessmentsReader.save(new DbOperator(context, new SummarizerDBContext()));
    }

    private static void processOEEnd(Telemetry event, IDBSession dbSession) {
        Logger.i(TAG, "Process OE END");
//        DbOperator dbOperator = new DbOperator(context, new SummarizerDBContext());
//        LearnerContentSummary summary = new LearnerContentSummary(event);
//        summary.save(dbOperator);
    }

    private static void processGEFeedbackEvent(Telemetry event, IDBSession dbSession) {
        Logger.i(TAG, "Process GE_FEEDBACK");
//        ContentFeedback contentFeedback = new ContentFeedback(event);
//        contentFeedback.save(new DbOperator(context));
    }

    private static void processGELaunchGameEvent(Telemetry event, IDBSession dbSession) {
        Logger.i(TAG, "Process GE_LAUNCH_GAME");

//        String uid = event.uid();
//
//        Map<String, Object> eks = (Map<String, Object>) event.edata().get("eks");
//        String identifier = (String) eks.get("gid");
//
//        DbOperator dbOperator = new DbOperator(context);
//        Content c = Content.find(dbOperator, identifier);
//
//        if (CONTENT_VISIBILITY_DEFAULT.equalsIgnoreCase(c.getVisibility())) {
//            ContentAccess contentAccess = new ContentAccess(uid, identifier, ACCESS_STATUS_VIEWED, c.getContentType());
//
//            if (contentAccess.exists(dbOperator)) {
//                contentAccess.update(dbOperator);
//            } else {
//                contentAccess.save(dbOperator);
//            }
//        }
    }

    private static void processOEInteractEvent(Telemetry event, IDBSession dbSession) {
        Logger.i(TAG, "Process OE_INTERACT");

//        Map<String, Object> eks = (Map<String, Object>) event.edata().get("eks");
//
//        if ("ContentApp-EndScreen".equalsIgnoreCase((String) eks.get("stageid"))) {
//            String uid = event.uid();
//            String identifier = (String) eks.get("id");
//
//            DbOperator dbOperator = new DbOperator(context);
//            Content c = Content.find(dbOperator, identifier);
//
//            if (CONTENT_VISIBILITY_DEFAULT.equalsIgnoreCase(c.getVisibility())) {
//                ContentAccess contentAccess = new ContentAccess(uid, identifier, ACCESS_STATUS_FULLY_PLAYED, c.getContentType());
//
//                if (contentAccess.exists(dbOperator)) {
//                    contentAccess.update(dbOperator);
//                } else {
//                    contentAccess.save(dbOperator);
//                }
//            }
//        }
    }
}
