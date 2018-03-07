package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class EnrolCourseRequest {

    private String userId;
    private String courseId;

    private EnrolCourseRequest(String userId, String courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public static class Builder {
        private String userId;
        private String courseId;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public Builder forCourse(String courseId) {
            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalArgumentException("courseId should not be null or empty.");
            }
            this.courseId = courseId;
            return this;
        }

        public EnrolCourseRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalStateException("courseId required.");
            }

            return new EnrolCourseRequest(userId, courseId);
        }
    }
}
