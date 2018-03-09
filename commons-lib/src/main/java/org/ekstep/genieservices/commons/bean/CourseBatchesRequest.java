package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class CourseBatchesRequest {

    private String[] courseIds;

    private CourseBatchesRequest(String[] courseIds) {
        this.courseIds = courseIds;
    }

    public String[] getCourseIds() {
        return courseIds;
    }

    public static class Builder {
        private String[] courseIds;

        public Builder forCourses(String[] courseIds) {
            this.courseIds = courseIds;
            return this;
        }

        public CourseBatchesRequest build() {
            return new CourseBatchesRequest(courseIds);
        }
    }
}
