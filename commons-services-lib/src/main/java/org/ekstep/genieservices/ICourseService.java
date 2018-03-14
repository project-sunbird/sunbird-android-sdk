package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesResponse;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;

/**
 * This is the interface with all the required APIs to perform necessary operations related to courses.
 */
public interface ICourseService {

    /**
     * This api is used to get the enrolled courses.
     *
     * @param enrolledCoursesRequest {@link EnrolledCoursesRequest}
     * @return {@link EnrolledCoursesResponse}
     */
    GenieResponse<EnrolledCoursesResponse> getEnrolledCourses(EnrolledCoursesRequest enrolledCoursesRequest);

    /**
     * This api is used to enroll the course.
     *
     * @param enrollCourseRequest {@link EnrollCourseRequest}
     * @return
     */
    GenieResponse<Void> enrollCourse(EnrollCourseRequest enrollCourseRequest);

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
     * @param courseBatchesRequest {@link CourseBatchesRequest}
     * @return
     */
    GenieResponse<CourseBatchesResponse> getCourseBatches(CourseBatchesRequest courseBatchesRequest);
}
