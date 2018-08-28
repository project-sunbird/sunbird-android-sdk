package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.UserProfileField;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/3/18.
 *
 * @author anil
 */
public class UserProfileDetailsRequest {

    private String userId;
    private List<String> requiredFields;
    private boolean refreshUserProfileDetails;
    private boolean returnRefreshedUserProfileDetails;

    private UserProfileDetailsRequest(String userId, List<String> requiredFields, boolean refreshUserProfileDetails, boolean returnRefreshedUserProfileDetails) {
        this.userId = userId;
        this.requiredFields = requiredFields;
        this.refreshUserProfileDetails = refreshUserProfileDetails;
        this.returnRefreshedUserProfileDetails = returnRefreshedUserProfileDetails;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public boolean isRefreshUserProfileDetails() {
        return refreshUserProfileDetails;
    }

    public boolean isReturnRefreshedUserProfileDetails() {
        return returnRefreshedUserProfileDetails;
    }

    public static class Builder {

        private String userId;
        private List<String> requiredFields;
        private boolean refreshUserProfileDetails;
        private boolean returnRefreshedUserProfileDetails;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public Builder addRequiredField(UserProfileField field) {
            if (this.requiredFields == null) {
                this.requiredFields = new ArrayList<>();
            }

            if (field == null) {
                throw new IllegalStateException("field should not be null.");
            }

            requiredFields.add(field.getValue());

            return this;
        }

        /**
         * The user profile details are refreshed from the server only if this flag is set.
         */
        public Builder refreshUserProfileDetailsFromServer() {
            this.refreshUserProfileDetails = true;
            return this;
        }

        /**
         * The user profile details are refreshed from the server only if this flag is set.
         */
        public Builder returnRefreshedUserProfileDetailsFromServer(boolean returnRefreshedUserProfileDetails) {
            this.returnRefreshedUserProfileDetails = returnRefreshedUserProfileDetails;
            return this;
        }

        public UserProfileDetailsRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            return new UserProfileDetailsRequest(userId, requiredFields, refreshUserProfileDetails, returnRefreshedUserProfileDetails);
        }
    }
}
