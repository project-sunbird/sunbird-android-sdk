package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/7/2017.
 *
 * @author anil
 */
public class ProfileImportRequest {

    private String sourceFilePath;

    private ProfileImportRequest(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public static class Builder {
        private String sourceFilePath;

        /**
         * Profile file path which needs to import
         */
        public Builder fromFilePath(String filePath) {
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException("Illegal filePath: " + filePath);
            }
            this.sourceFilePath = filePath;
            return this;
        }

        public ProfileImportRequest build() {
            if (sourceFilePath == null) {
                throw new IllegalStateException("filePath required.");
            }

            return new ProfileImportRequest(sourceFilePath);
        }
    }
}
