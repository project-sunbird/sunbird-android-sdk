package org.ekstep.genieservices.commons.bean;

import java.io.File;
import java.util.List;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentImportRequest {

    private boolean isChildContent;
    private File destinationFolder;
    private String sourceFilePath;
    private List<String> contentIds;

    public ContentImportRequest(boolean isChildContent, File destinationFolder, String sourceFilePath, List<String> contentIds) {
        this.isChildContent = isChildContent;
        this.destinationFolder = destinationFolder;
        this.sourceFilePath = sourceFilePath;
        this.contentIds = contentIds;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public static class Builder {
        private boolean isChildContent;
        private File destinationFolder;
        private String sourceFilePath;
        private List<String> contentIds;

        /**
         * @param isChildContent Should be True if importing nested content of any collection/textbook else False.
         */
        public Builder(boolean isChildContent) {
            this.isChildContent = isChildContent;
        }

        /**
         * Content file path which needs to import
         */
        public Builder fromFilePath(String filePath) {
            if (filePath == null) {
                throw new IllegalArgumentException("Illegal filePath: " + filePath);
            }
            this.sourceFilePath = filePath;
            return this;
        }

        /**
         * Destination folder where content will import.
         */
        public Builder toFolder(File toFolder) {
            if (toFolder == null) {
                throw new IllegalArgumentException("Illegal toFolder");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        /**
         * List of content identifier which needs to import.
         */
        public Builder contentIds(List<String> contentIds) {
            this.contentIds = contentIds;
            return this;
        }

        public ContentImportRequest build() {
            if (destinationFolder == null) {
                throw new IllegalStateException("To folder required.");
            }

            if (sourceFilePath == null && contentIds == null) {
                throw new IllegalStateException("Provide either fromFilePath or contentIds.");
            }

            return new ContentImportRequest(isChildContent, destinationFolder, sourceFilePath, contentIds);
        }
    }
}
