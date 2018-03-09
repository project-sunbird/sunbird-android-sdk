package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class CourseHandler {

    public static GenieResponse fetchEnrolledCoursesFromServer(AppContext appContext, Session sessionData, String userId) {
        return null;
    }

    public static void refreshEnrolledCoursesFromServer(AppContext appContext, Session sessionData, String userId, NoSqlModel enrolledCoursesInDB) {

    }

    public static GenieResponse fetchCourseBatchesFromServer(AppContext appContext, Session sessionData, CourseBatchesRequest courseBatchesRequest) {
        return null;
    }
}
