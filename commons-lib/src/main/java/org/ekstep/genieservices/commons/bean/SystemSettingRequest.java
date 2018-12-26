package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

public class SystemSettingRequest {

    private String id;
    private String filePath;

    private SystemSettingRequest(String id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public static class Builder {
        private String id;
        private String filePath;

        public Builder forId(String id) {
            if (StringUtil.isNullOrEmpty(id)) {
                throw new IllegalArgumentException("id should not be null or empty.");
            }
            this.id = id;
            return this;
        }

        public Builder fromFilePath(String filePath) {
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException("filePath should not be null or empty.");
            }

            this.filePath = filePath;
            return this;
        }

        public SystemSettingRequest build() {
            if (StringUtil.isNullOrEmpty(id)) {
                throw new IllegalStateException("id required.");
            }

            return new SystemSettingRequest(id, filePath);
        }
    }
}
