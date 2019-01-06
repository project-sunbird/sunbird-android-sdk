package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ICourseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Batch;
import org.ekstep.genieservices.commons.bean.BatchDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentStateResponse;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.CourseBatchesResponse;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesRequest;
import org.ekstep.genieservices.commons.bean.EnrolledCoursesResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.GetContentStateRequest;
import org.ekstep.genieservices.commons.bean.UnenrolCourseRequest;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;

/**
 * This class provides APIs for performing {@link CourseService} related operations on a separate thread.
 */
public class CourseService {

    private ICourseService courseService;

    public CourseService(GenieService genieService) {
        this.courseService = genieService.getCourseService();
    }

    /**
     * This api is used to get the enrolled courses.
     *
     * @param enrolledCoursesRequest - {@link EnrolledCoursesRequest}
     * @param responseHandler        - {@link IResponseHandler <EnrolledCoursesResponse>}
     */
    public void getEnrolledCourses(final EnrolledCoursesRequest enrolledCoursesRequest, IResponseHandler<EnrolledCoursesResponse> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<EnrolledCoursesResponse>() {
            @Override
            public GenieResponse<EnrolledCoursesResponse> perform() {
                return courseService.getEnrolledCourses(enrolledCoursesRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to enroll the course.
     *
     * @param enrollCourseRequest - {@link EnrollCourseRequest}
     * @param responseHandler     - {@link IResponseHandler <Void>}
     */
    public void enrollCourse(final EnrollCourseRequest enrollCourseRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return courseService.enrollCourse(enrollCourseRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to unenroll the course.
     *
     * @param unenrolCourseRequest - {@link UnenrolCourseRequest}
     * @param responseHandler       - {@link IResponseHandler <Void>}
     */
    public void unenrolCourse(final UnenrolCourseRequest unenrolCourseRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return courseService.unenrolCourse(unenrolCourseRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to update the content state of course.
     *
     * @param updateContentStateRequest - {@link UpdateContentStateRequest}
     * @param responseHandler           - {@link IResponseHandler <Void>}
     */
    public void updateContentState(final UpdateContentStateRequest updateContentStateRequest, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return courseService.updateContentState(updateContentStateRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get available batches for course.
     *
     * @param courseBatchesRequest - {@link CourseBatchesRequest}
     * @param responseHandler      - {@link IResponseHandler <CourseBatchesResponse>}
     */
    public void getCourseBatches(final CourseBatchesRequest courseBatchesRequest, IResponseHandler<CourseBatchesResponse> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<CourseBatchesResponse>() {
            @Override
            public GenieResponse<CourseBatchesResponse> perform() {
                return courseService.getCourseBatches(courseBatchesRequest);
            }
        }, responseHandler);
    }

    public void getBatchDetails(final BatchDetailsRequest batchDetailsRequest, IResponseHandler<Batch> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Batch>() {
            @Override
            public GenieResponse<Batch> perform() {
                return courseService.getBatchDetails(batchDetailsRequest);
            }
        }, responseHandler);
    }

    /**
     * This api is used to get the state of content within a course for a particular batch
     *
     * @param contentStateRequest
     * @return
     */
    public void getContentState(final GetContentStateRequest contentStateRequest, IResponseHandler<ContentStateResponse> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<ContentStateResponse>() {
            @Override
            public GenieResponse<ContentStateResponse> perform() {
                return courseService.getContentState(contentStateRequest);
            }
        }, responseHandler);
    }
}
