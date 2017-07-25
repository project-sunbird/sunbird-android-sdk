package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts sourceFilePath while requesting for import.
 *
 */
public class TelemetryImportRequest {

    private String sourceFilePath;

    private TelemetryImportRequest(String sourceFilePath) {
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

        public TelemetryImportRequest build() {
            if (sourceFilePath == null) {
                throw new IllegalStateException("filePath required.");
            }

            return new TelemetryImportRequest(sourceFilePath);
        }
    }
}
