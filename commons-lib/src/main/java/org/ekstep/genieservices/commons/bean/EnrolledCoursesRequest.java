package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class EnrolledCoursesRequest {

    private String userId;
    private boolean refreshEnrolledCourses;

    private EnrolledCoursesRequest(String userId, boolean refreshEnrolledCourses) {
        this.userId = userId;
        this.refreshEnrolledCourses = refreshEnrolledCourses;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isRefreshEnrolledCourses() {
        return refreshEnrolledCourses;
    }

    public static class Builder {
        private String userId;
        private boolean refreshEnrolledCourses;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        /**
         * The enrolled courses are refreshed from the server only if this flag is set.
         */
        public Builder refreshEnrolledCoursesFromServer() {
            this.refreshEnrolledCourses = true;
            return this;
        }

        public EnrolledCoursesRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            return new EnrolledCoursesRequest(userId, refreshEnrolledCourses);
        }
    }
}
