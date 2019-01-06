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
    private CourseBatchStatus[] status;
    private String enrollmentType;
    private String sortBy;

    public CourseBatchesRequest(String courseId, CourseBatchStatus[] status, String enrollmentType, String sortOrder) {
        this.courseId = courseId;
        this.status = status;
        this.enrollmentType = enrollmentType;
        this.sortBy = sortOrder;
    }

    public String getCourseId() {
        return courseId;
    }

    public CourseBatchStatus[] getStatus() {
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
        private CourseBatchStatus[] status;
        private String enrollmentType;
        private String sort_by;

        public Builder forCourse(String courseId) {
            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalArgumentException("courseId required.");
            }

            this.courseId = courseId;
            return this;
        }

        public Builder withStatus(CourseBatchStatus[] status) {
            this.status = status;
            return this;
        }

        public Builder forEnrollmentType(CourseEnrollmentType enrollmentType) {
            this.enrollmentType = enrollmentType.getValue();
            return this;
        }

        public Builder sortBy(SortOrder sortBy) {
            this.sort_by = sortBy.getValue();
            return this;
        }

        public CourseBatchesRequest build() {
            return new CourseBatchesRequest(courseId, status, enrollmentType, sort_by);
        }
    }
}
