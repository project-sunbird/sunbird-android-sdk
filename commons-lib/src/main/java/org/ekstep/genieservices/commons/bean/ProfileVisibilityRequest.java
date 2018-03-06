package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.UserProfileField;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/3/18.
 * shriharsh
 */

public class ProfileVisibilityRequest {

    //todo Check if the userId has to be asked
    private String userId;
    private List<String> privateFields;
    private List<String> publicFields;

    public String getUserId() {
        return userId;
    }

    public List<String> getPrivateFields() {
        return privateFields;
    }

    public List<String> getPublicFields() {
        return publicFields;
    }

    private ProfileVisibilityRequest(String userId, List<String> privateFields, List<String> publicFields) {
        this.userId = userId;
        this.privateFields = privateFields;
        this.publicFields = publicFields;
    }

    public static class Builder {

        private String userId;
        private List<String> privateFieldsList;
        private List<String> publicFieldsList;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId required.");
            }

            this.userId = userId;
            return this;
        }


        public Builder addPrivateField(UserProfileField privateField) {
            if (privateField == null) {
                throw new IllegalStateException(" private field is required.");
            }

            if (this.privateFieldsList == null) {
                this.privateFieldsList = new ArrayList<>();
            }

            privateFieldsList.add(privateField.getValue());

            return this;
        }

        public Builder addPrivateFields(List<UserProfileField> privateFields) {
            if (privateFields == null) {
                throw new IllegalStateException("private fields are required.");
            }

            if (this.privateFieldsList == null) {
                this.privateFieldsList = new ArrayList<>();
            }

            for (UserProfileField userProfileField : privateFields) {
                privateFieldsList.add(userProfileField.getValue());
            }

            return this;
        }

        public Builder addPublicField(UserProfileField publicField) {
            if (publicField == null) {
                throw new IllegalStateException(" public field is required.");
            }

            if (this.publicFieldsList == null) {
                this.publicFieldsList = new ArrayList<>();
            }

            publicFieldsList.add(publicField.getValue());

            return this;
        }

        public Builder addPublicFields(List<UserProfileField> publicFields) {
            if (publicFields == null) {
                throw new IllegalStateException("public fields are required.");
            }

            if (this.publicFieldsList == null) {
                this.publicFieldsList = new ArrayList<>();
            }

            for (UserProfileField userProfileField : publicFields) {
                publicFieldsList.add(userProfileField.getValue());
            }

            return this;
        }

        public ProfileVisibilityRequest build() {
            if (CollectionUtil.isNullOrEmpty(privateFieldsList) && CollectionUtil.isNullOrEmpty(publicFieldsList)) {
                throw new IllegalStateException("Both public and private fields cannot be empty ");
            }

            return new ProfileVisibilityRequest(userId, privateFieldsList, publicFieldsList);
        }
    }

}
