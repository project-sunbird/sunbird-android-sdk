package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 7/3/18.
 * shriharsh
 */

public class UploadFileRequest {

    private String filePath;
    private String userId;

    public UploadFileRequest(String filePath, String userId) {
        this.filePath = filePath;
        this.userId = "user/" + userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUserId() {
        return userId;
    }

    public static class Builder {

        private String filePath;
        private String userId;

        public Builder file(String filePath) {
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException("file path cannot be empty");
            }

            this.filePath = filePath;
            return this;
        }


        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("user id cannot be empty");
            }

            this.userId = userId;
            return this;
        }

        public UploadFileRequest build() {

            return new UploadFileRequest(filePath, userId);
        }

    }

}
