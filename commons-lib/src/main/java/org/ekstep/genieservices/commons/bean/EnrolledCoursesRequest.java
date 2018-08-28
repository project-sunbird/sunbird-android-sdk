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
    private boolean returnRefreshedEnrolledCourses;

    private EnrolledCoursesRequest(String userId, boolean refreshEnrolledCourses, boolean returnRefreshedEnrolledCourses) {
        this.userId = userId;
        this.refreshEnrolledCourses = refreshEnrolledCourses;
        this.returnRefreshedEnrolledCourses = returnRefreshedEnrolledCourses;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isRefreshEnrolledCourses() {
        return refreshEnrolledCourses;
    }

    public boolean isReturnRefreshedEnrolledCourses() {
        return returnRefreshedEnrolledCourses;
    }

    public static class Builder {
        private String userId;
        private boolean refreshEnrolledCourses;
        private boolean returnRefreshedEnrolledCourses;

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

        /**
         * The enrolled courses are refreshed from the server only if this flag is set and returned immediately.
         */
        public Builder returnRefreshedEnrolledCoursesFromServer() {
            this.returnRefreshedEnrolledCourses = true;
            return this;
        }

        public EnrolledCoursesRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            return new EnrolledCoursesRequest(userId, refreshEnrolledCourses, returnRefreshedEnrolledCourses);
        }
    }
}
