package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 6/6/2017.
 *
 * @author anil
 */
public class ContentImportRequest {

    private boolean isChildContent;
    private String destinationFolder;
    private List<String> contentIds;

    private ContentImportRequest(boolean isChildContent, String destinationFolder, List<String> contentIds) {
        this.isChildContent = isChildContent;
        this.destinationFolder = destinationFolder;
        this.contentIds = contentIds;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public static class Builder {
        private boolean isChildContent;
        private String destinationFolder;
        private List<String> contentIds;

        /**
         * Method to indicate that the file being imported is a child content
         */
        public Builder childContent() {
            this.isChildContent = true;
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

            if (contentIds == null || contentIds.size() == 0) {
                throw new IllegalStateException("ContentIds required.");
            }

            return new ContentImportRequest(isChildContent, destinationFolder, contentIds);
        }
    }
}
