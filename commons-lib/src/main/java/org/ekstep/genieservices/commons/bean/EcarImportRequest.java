package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class EcarImportRequest {

    private boolean isChildContent;
    private String destinationFolder;
    private String sourceFilePath;

    private EcarImportRequest(boolean isChildContent, String destinationFolder, String sourceFilePath) {
        this.isChildContent = isChildContent;
        this.destinationFolder = destinationFolder;
        this.sourceFilePath = sourceFilePath;
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

        public EcarImportRequest build() {
            if (destinationFolder == null) {
                throw new IllegalStateException("Destination folder required.");
            }

            if (StringUtil.isNullOrEmpty(sourceFilePath)) {
                throw new IllegalStateException("fromFilePath required.");
            }

            return new EcarImportRequest(isChildContent, destinationFolder, sourceFilePath);
        }
    }
}
