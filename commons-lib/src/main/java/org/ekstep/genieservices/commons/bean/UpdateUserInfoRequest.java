package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 3/23/18.
 *
 * @author Indraja
 */
public class UpdateUserInfoRequest {

    private String userId;

    private UpdateUserInfoRequest(String userId) {
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

        public UpdateUserInfoRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            return new UpdateUserInfoRequest(userId);
        }
    }
}
