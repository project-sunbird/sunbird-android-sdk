package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.EnrolCourseRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.network.EnrolCourseAPI;
import org.ekstep.genieservices.content.network.EnrolledCoursesAPI;
import org.ekstep.genieservices.content.network.UpdateContentStateAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class CourseHandler {

    private static Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
    }

    public static GenieResponse fetchEnrolledCoursesFromServer(AppContext appContext, Session sessionData, String userId) {
        EnrolledCoursesAPI enrolledCoursesAPI = new EnrolledCoursesAPI(appContext, getCustomHeaders(sessionData), userId);
        return enrolledCoursesAPI.get();
    }

    public static void refreshEnrolledCoursesFromServer(final AppContext appContext, final Session sessionData,
                                                        final String userId, final NoSqlModel enrolledCoursesInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse enrolledCoursesAPIResponse = fetchEnrolledCoursesFromServer(appContext, sessionData, userId);
                if (enrolledCoursesAPIResponse.getStatus()) {
                    String jsonResponse = enrolledCoursesAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        enrolledCoursesInDB.setValue(jsonResponse);
                        enrolledCoursesInDB.update();
                    }
                }
            }
        }).start();
    }


    public static GenieResponse enrolCourseInServer(AppContext appContext, Session sessionData, EnrolCourseRequest enrolCourseRequest) {
        EnrolCourseAPI enrolCourseAPI = new EnrolCourseAPI(appContext, getCustomHeaders(sessionData),
                getEnrolCourseRequest(enrolCourseRequest));
        return enrolCourseAPI.post();
    }

    private static Map<String, Object> getEnrolCourseRequest(EnrolCourseRequest enrolCourseRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", enrolCourseRequest.getUserId());
        requestMap.put("contentId", enrolCourseRequest.getContentId());
        requestMap.put("courseId", enrolCourseRequest.getCourseId());
        requestMap.put("batchId", enrolCourseRequest.getBatchId());
        return requestMap;
    }

    public static GenieResponse updateContentStateInServer(AppContext appContext, Session sessionData,
                                                           UpdateContentStateRequest updateContentStateRequest) {

        UpdateContentStateAPI updateContentStateAPI = new UpdateContentStateAPI(appContext, getCustomHeaders(sessionData),
                getUpdateContentStateRequest(updateContentStateRequest));

//      // todo:  return updateContentStateAPI.patch();
        return null;
    }

    private static Map<String, Object> getUpdateContentStateRequest(UpdateContentStateRequest updateContentStateRequest) {
        Map<String, Object> contentsMap = new HashMap<>();
        contentsMap.put("contentId", updateContentStateRequest.getContentId());
        contentsMap.put("courseId", updateContentStateRequest.getCourseId());
        contentsMap.put("batchId", updateContentStateRequest.getBatchId());
        contentsMap.put("status", updateContentStateRequest.getStatus());
        contentsMap.put("progress", updateContentStateRequest.getProgress());

        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getResult())) {
            contentsMap.put("result", updateContentStateRequest.getResult());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getGrade())) {
            contentsMap.put("grade", updateContentStateRequest.getGrade());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getScore())) {
            contentsMap.put("score", updateContentStateRequest.getScore());
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", updateContentStateRequest.getUserId());
        requestMap.put("contents", contentsMap);
        return requestMap;
    }

    public static GenieResponse fetchCourseBatchesFromServer(AppContext appContext, Session sessionData,
                                                             CourseBatchesRequest courseBatchesRequest) {
        return null;
    }
}
