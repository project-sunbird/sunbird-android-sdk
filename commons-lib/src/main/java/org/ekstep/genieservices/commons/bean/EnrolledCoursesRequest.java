package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class EnrolledCoursesRequest {

    private String userId;

    private EnrolledCoursesRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public static class Builder {
        private String userId;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public EnrolledCoursesRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            return new EnrolledCoursesRequest(userId);
        }
    }
}
