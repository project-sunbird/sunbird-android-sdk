package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * This class accepts isChildContent, destinationFolder name, sourceFilePath name and {@link List<CorrelationData>} when requesting to import an ecar.
 */
public class EcarImportRequest {

    private boolean isChildContent;
    private String destinationFolder;
    private String sourceFilePath;
    private List<CorrelationData> correlationData;

    private EcarImportRequest(boolean isChildContent, String destinationFolder, String sourceFilePath, List<CorrelationData> correlationData) {
        this.isChildContent = isChildContent;
        this.destinationFolder = destinationFolder;
        this.sourceFilePath = sourceFilePath;
        this.correlationData = correlationData;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public static class Builder {
        private boolean isChildContent;
        private String destinationFolder;
        private String sourceFilePath;
        private List<CorrelationData> correlationData;

        /**
         * Method to indicate that the file being imported is a child content
         */
        public Builder isChildContent() {
            this.isChildContent = true;
            return this;
        }

        /**
         * Content file path which needs to import
         */
        public Builder fromFilePath(String filePath) {
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException("Illegal filePath: " + filePath);
            }
            this.sourceFilePath = filePath;
            return this;
        }

        /**
         * Destination folder where content will import.
         */
        public Builder toFolder(String toFolder) {
            if (toFolder == null) {
                throw new IllegalArgumentException("Illegal toFolder");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        /**
         * CorrelationData of content.
         */
        public Builder correlationData(List<CorrelationData> correlationData) {
            this.correlationData = correlationData;
            return this;
        }

        public EcarImportRequest build() {
            if (destinationFolder == null) {
                throw new IllegalStateException("Destination folder required.");
            }

            if (StringUtil.isNullOrEmpty(sourceFilePath)) {
                throw new IllegalStateException("fromFilePath required.");
            }

            return new EcarImportRequest(isChildContent, destinationFolder, sourceFilePath, correlationData);
        }
    }
}
