package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts sourceFilePath while requesting for import.
 *
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
         * Absolute path of profile file which needs to imported.
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
