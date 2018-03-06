package org.ekstep.genieservices.content;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Batch;
import org.ekstep.genieservices.commons.bean.BatchesForCourseRequest;
import org.ekstep.genieservices.commons.bean.Course;
import org.ekstep.genieservices.commons.bean.EnrolCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;

import java.util.List;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class CourseServiceImpl extends BaseService implements ICourseService {

    private IAuthSession<Session> authSession;

    public CourseServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);
        this.authSession = authSession;
    }

    @Override
    public GenieResponse<List<Course>> getEnrolledCourses(EnrolledCoursesRequest enrolledCoursesRequest) {
        return null;
    }

    @Override
    public GenieResponse<Void> enrolCourse(EnrolCourseRequest enrolCourseRequest) {
        return null;
    }

    @Override
    public GenieResponse<Void> updateContentState(UpdateContentStateRequest updateContentStateRequest) {
        return null;
    }

    @Override
    public GenieResponse<List<Batch>> getBatches(BatchesForCourseRequest batchesForCourseRequest) {
        return null;
    }
}
