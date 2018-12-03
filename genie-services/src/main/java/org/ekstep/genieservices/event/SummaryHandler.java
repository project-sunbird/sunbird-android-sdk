package org.ekstep.genieservices.event;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.ContentState;
import org.ekstep.genieservices.commons.bean.ContentStateResponse;
import org.ekstep.genieservices.commons.bean.GenericEvent;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GetContentStateRequest;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.bean.telemetry.ProducerData;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.eventbus.EventBus;
import org.ekstep.genieservices.profile.SummarizerServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 9/5/17.
 */

public class SummaryHandler {

    private static final String TAG = SummaryHandler.class.getSimpleName();
    private static final String COURSE_STATUS_UPDATED_SUCCESSFULLY = "COURSE_STATUS_UPDATED_SUCCESSFULLY";

    // Data to be stored in these variable when START event is read
    private static String CONTENT_ID = null;
    private static String UID = null;
    private static Map<String, Object> contentContextMap = null;


    public static void handleTelemetryEvent(Telemetry event, AppContext appContext) {
        if (event == null) {
            return;
        }

        if ("START".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            if (contentContextMap != null) {
                callUpdateContentStateAPI(event, event.getEid());
            }

            processOEStart(event, appContext);
        } else if ("START".equals(event.getEid()) && checkIsCourse(event)) {
            String contentContext = appContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.SUNBIRD_CONTENT_CONTEXT, "");

            if (!StringUtil.isNullOrEmpty(contentContext)) {
                contentContextMap = GsonUtil.fromJson(contentContext, Map.class);
            }
        } else if ("ASSESS".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            processOEAssess(event, appContext);
        } else if ("END".equals(event.getEid()) && checkIsCourse(event)) {
            if (contentContextMap != null) {
                //update content state to empty
                contentContextMap = null;
                appContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SUNBIRD_CONTENT_CONTEXT, "");
            }
        } else if ("END".equals(event.getEid()) && checkPdata(event.getContext().getPdata())) {
            if (contentContextMap != null) {
                callUpdateContentStateAPI(event, event.getEid());
            }

            processOEEnd(event, appContext);
        }
    }

    private static void callUpdateContentStateAPI(Telemetry event, String eventType) {
        ICourseService courseService = GenieService.getService().getCourseService();

        String userId = (String) contentContextMap.get("userId");
        String courseId = (String) contentContextMap.get("courseId");
        String batchId = (String) contentContextMap.get("batchId");
        String contentId = event.getObject().getId();

        int contentStatus = checkStatusOfContent(userId, courseId, batchId, contentId, courseService);

        if (eventType.equalsIgnoreCase("start") && contentStatus == 0) {
            //update content state to 1 and progress to 5
            courseService.updateContentState(getUpdateContentStateRequest(userId, courseId, batchId, contentId, 1, 5));
        } else if ((eventType.equalsIgnoreCase("end") && contentStatus == 0)
                || (eventType.equalsIgnoreCase("end") && contentStatus == 1)) {
            //update content state to  2 and progress to 100
            GenieResponse<Void> response = courseService.updateContentState(getUpdateContentStateRequest(userId, courseId, batchId, contentId, 2, 100));

            //fire an event to be handled on mobile side to call the getEnrolledCourses again if the update was successful
            if (response.getStatus()) {
                EventBus.postEvent(new GenericEvent(COURSE_STATUS_UPDATED_SUCCESSFULLY));
            }
        }
    }

    private static UpdateContentStateRequest getUpdateContentStateRequest(String userId, String courseId, String batchId, String contentId, int status, int progress) {
        return new UpdateContentStateRequest.Builder()
                .forUser(userId)
                .forContent(contentId)
                .forBatch(batchId)
                .forCourse(courseId)
                .status(status)
                .progress(progress)
                .build();
    }

    private static int checkStatusOfContent(String userId, String courseId, String batchId, String contentId, ICourseService courseService) {
        ArrayList<String> contentIds = new ArrayList<>();
        contentIds.add(contentId);

        ArrayList<String> courseIds = new ArrayList<>();
        courseIds.add(courseId);

        GetContentStateRequest getContentStateRequest = new GetContentStateRequest.Builder().forUser(userId)
                .forBatch(batchId)
                .forContents(contentIds)
                .forCourses(courseIds)
                .build();

        GenieResponse<ContentStateResponse> contentStateResponse = courseService.getContentState(getContentStateRequest);
        List<ContentState> contentStateList = contentStateResponse.getResult().getContentList();

        for (ContentState contentState : contentStateList) {
            if (contentState.getContentId().equalsIgnoreCase(contentId)) {
                return contentState.getStatus();
            }
        }

        return 0;
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

    private static boolean checkIsCourse(Telemetry event) {

        if (event != null && event.getObject() != null && event.getObject().getType().equalsIgnoreCase("course")) {
            return true;
        }

        return false;
    }

}