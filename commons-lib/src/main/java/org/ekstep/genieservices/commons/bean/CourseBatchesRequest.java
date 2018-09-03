package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.CourseBatchStatus;
import org.ekstep.genieservices.commons.bean.enums.CourseEnrollmentType;
import org.ekstep.genieservices.commons.bean.enums.SortOrder;
import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class CourseBatchesRequest {

    private String courseId;
    private String status;
    private String enrollmentType;
    private String sortBy;

    public CourseBatchesRequest(String courseId, CourseBatchStatus status, CourseEnrollmentType enrollmentType, SortOrder sortOrder) {
        this.courseId = courseId;
        this.status = status.getValue();
        this.enrollmentType = enrollmentType.getValue();
        this.sortBy = sortOrder.getValue();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public String getEnrollmentType() {
        return enrollmentType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public static class Builder {
        private String courseId;
        private CourseBatchStatus status;
        private CourseEnrollmentType enrollmentType;
        private SortOrder sort_by;

        public Builder forCourse(String courseId) {
            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalArgumentException("courseId required.");
            }

            this.courseId = courseId;
            return this;
        }

        public Builder withStatus(CourseBatchStatus status) {
            this.status = status;
            return this;
        }

        public Builder forEnrollmentType(CourseEnrollmentType enrollmentType) {
            this.enrollmentType = enrollmentType;
            return this;
        }

        public Builder sortBy(SortOrder sortBy) {
            this.sort_by = sortBy;
            return this;
        }

        public CourseBatchesRequest build() {
            return new CourseBatchesRequest(courseId, status, enrollmentType, sort_by);
        }
    }
}
