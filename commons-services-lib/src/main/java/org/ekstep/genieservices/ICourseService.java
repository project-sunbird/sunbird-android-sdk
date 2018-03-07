package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Batch;
import org.ekstep.genieservices.commons.bean.BatchesForCourseRequest;
import org.ekstep.genieservices.commons.bean.Course;
import org.ekstep.genieservices.commons.bean.EnrolCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;

import java.util.List;

/**
 * This is the interface with all the required APIs to perform necessary operations related to courses.
 */
public interface ICourseService {

    /**
     * This api is used to get the enrolled courses.
     *
     * @param enrolledCoursesRequest {@link EnrolledCoursesRequest}
     * @return {@link List<Course>}
     */
    GenieResponse<List<Course>> getEnrolledCourses(EnrolledCoursesRequest enrolledCoursesRequest);

    /**
     * This api is used to enrol the course.
     *
     * @param enrolCourseRequest {@link EnrolCourseRequest}
     * @return
     */
    GenieResponse<Void> enrolCourse(EnrolCourseRequest enrolCourseRequest);

    /**
     * This api is used to update the content state of course.
     *
     * @param updateContentStateRequest {@link UpdateContentStateRequest}
     * @return
     */
    GenieResponse<Void> updateContentState(UpdateContentStateRequest updateContentStateRequest);

    /**
     * This api is used to get available batches for course.
     *
     * @param batchesForCourseRequest {@link BatchesForCourseRequest}
     * @return
     */
    GenieResponse<List<Batch>> getBatches(BatchesForCourseRequest batchesForCourseRequest);
}
