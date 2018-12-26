package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GetContentStateRequest;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UnenrolCourseRequest;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.network.BatchDetailsAPI;
import org.ekstep.genieservices.content.network.ContentStateAPI;
import org.ekstep.genieservices.content.network.CourseBatchesAPI;
import org.ekstep.genieservices.content.network.EnrolCourseAPI;
import org.ekstep.genieservices.content.network.EnrolledCoursesAPI;
import org.ekstep.genieservices.content.network.UnenrolCourseAPI;
import org.ekstep.genieservices.content.network.UpdateContentStateAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static GenieResponse enrolCourseInServer(AppContext appContext, Session sessionData, EnrollCourseRequest enrollCourseRequest) {
        EnrolCourseAPI enrolCourseAPI = new EnrolCourseAPI(appContext, getCustomHeaders(sessionData),
                getEnrolCourseRequest(enrollCourseRequest));
        return enrolCourseAPI.post();
    }

    private static Map<String, Object> getEnrolCourseRequest(EnrollCourseRequest enrollCourseRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", enrollCourseRequest.getUserId());
        requestMap.put("contentId", enrollCourseRequest.getContentId());
        requestMap.put("courseId", enrollCourseRequest.getCourseId());
        requestMap.put("batchId", enrollCourseRequest.getBatchId());
        return requestMap;
    }

    public static GenieResponse unenrolCourseInServer(AppContext appContext, Session sessionData, UnenrolCourseRequest unenrolCourseRequest) {
        UnenrolCourseAPI unenrollCourseAPI = new UnenrolCourseAPI(appContext, getCustomHeaders(sessionData),
                getUnenrolCourseRequest(unenrolCourseRequest));
        return unenrollCourseAPI.post();
    }

    private static Map<String, Object> getUnenrolCourseRequest(UnenrolCourseRequest unenrolCourseRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", unenrolCourseRequest.getUserId());
        requestMap.put("courseId", unenrolCourseRequest.getCourseId());
        requestMap.put("batchId", unenrolCourseRequest.getBatchId());
        return requestMap;
    }

    public static GenieResponse updateContentStateInServer(AppContext appContext, Session sessionData,
                                                           UpdateContentStateRequest updateContentStateRequest) {

        UpdateContentStateAPI updateContentStateAPI = new UpdateContentStateAPI(appContext, getCustomHeaders(sessionData),
                getUpdateContentStateRequest(updateContentStateRequest));
        return updateContentStateAPI.patch();
    }

    public static GenieResponse updateContentStateListInServer(AppContext appContext, Session sessionData,
                                                               Map<String, List<UpdateContentStateRequest>> updateContentStateListRequest) {
        GenieResponse genieResponse = null;

        for (Map.Entry<String, List<UpdateContentStateRequest>> requestMap : updateContentStateListRequest.entrySet()) {

            UpdateContentStateAPI updateContentStateAPI = new UpdateContentStateAPI(appContext, getCustomHeaders(sessionData),
                    getUpdateContentStateListRequest(requestMap.getKey(), requestMap.getValue()));

            genieResponse = updateContentStateAPI.patch();

            if (!genieResponse.getStatus()) {
                break;
            }
        }

        return genieResponse;
    }

    private static Map<String, Object> getUpdateContentStateListRequest(String userId, List<UpdateContentStateRequest> updateContentStateListRequest) {
        List<Map<String, Object>> contents = new ArrayList<>();

        for (UpdateContentStateRequest updateContentStateRequest : updateContentStateListRequest) {
            contents.add(getRequestMap(updateContentStateRequest));
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        requestMap.put("contents", contents);
        return requestMap;
    }


    private static Map<String, Object> getUpdateContentStateRequest(UpdateContentStateRequest updateContentStateRequest) {
        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(getRequestMap(updateContentStateRequest));

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", updateContentStateRequest.getUserId());
        requestMap.put("contents", contents);
        return requestMap;
    }

    private static Map<String, Object> getRequestMap(UpdateContentStateRequest updateContentStateRequest) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("contentId", updateContentStateRequest.getContentId());
        contentMap.put("courseId", updateContentStateRequest.getCourseId());
        contentMap.put("batchId", updateContentStateRequest.getBatchId());
        contentMap.put("status", updateContentStateRequest.getStatus());
        contentMap.put("progress", updateContentStateRequest.getProgress());

        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getResult())) {
            contentMap.put("result", updateContentStateRequest.getResult());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getGrade())) {
            contentMap.put("grade", updateContentStateRequest.getGrade());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getScore())) {
            contentMap.put("score", updateContentStateRequest.getScore());
        }
        return contentMap;
    }

    public static GenieResponse fetchCourseBatchesFromServer(AppContext appContext, Session sessionData,
                                                             Map<String, Object> courseBatchesRequest) {
        CourseBatchesAPI courseBatchesAPI = new CourseBatchesAPI(appContext, getCustomHeaders(sessionData),
                courseBatchesRequest);
        return courseBatchesAPI.post();
    }

    public static GenieResponse fetchBatchDetailsFromServer(AppContext appContext, Session sessionData,
                                                            String batchId) {
        BatchDetailsAPI batchDetailsAPI = new BatchDetailsAPI(appContext, getCustomHeaders(sessionData), batchId);
        return batchDetailsAPI.get();
    }

    public static GenieResponse fetchContentStateFromServer(AppContext appContext, Session sessionData, GetContentStateRequest contentStateRequest) {
        ContentStateAPI enrolCourseAPI = new ContentStateAPI(appContext, getCustomHeaders(sessionData),
                getCourseContentStateRequest(contentStateRequest));
        return enrolCourseAPI.post();
    }

    private static Map<String, Object> getCourseContentStateRequest(GetContentStateRequest contentStateRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", contentStateRequest.getUserId());
        requestMap.put("courseIds", contentStateRequest.getCourseIds());
        requestMap.put("contentIds", contentStateRequest.getContentIds());
        requestMap.put("batchId", contentStateRequest.getBatchId());
        return requestMap;
    }
}
