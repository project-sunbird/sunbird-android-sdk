package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ICourseService;

/**
 * This class provides APIs for performing {@link CourseService} related operations on a separate thread.
 */
public class CourseService {

    private ICourseService courseService;

    public CourseService(GenieService genieService) {
        this.courseService = genieService.getCourseService();
    }
}
