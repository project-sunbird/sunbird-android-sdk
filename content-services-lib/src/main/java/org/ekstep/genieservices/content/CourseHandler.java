package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.network.EnrolledCoursesAPI;

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

    public static GenieResponse fetchCourseBatchesFromServer(AppContext appContext, Session sessionData, CourseBatchesRequest courseBatchesRequest) {
        return null;
    }
}
