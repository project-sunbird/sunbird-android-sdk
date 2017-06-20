package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/7/2017.
 *
 * @author anil
 */
public class ImportRequest {

    private String sourceFilePath;

    private ImportRequest(String sourceFilePath) {
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

        public ImportRequest build() {
            if (sourceFilePath == null) {
                throw new IllegalStateException("filePath required.");
            }

            return new ImportRequest(sourceFilePath);
        }
    }
}
