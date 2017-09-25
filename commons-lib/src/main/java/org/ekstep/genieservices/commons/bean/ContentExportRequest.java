package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * This class accepts list of contentId's, and destination folder while building the request, and is used when requesting content export.
 */
public class ContentExportRequest {

    private List<String> contentIds;
    private String destinationFolder;

    private ContentExportRequest(List<String> contentIds, String destinationFolder) {
        this.contentIds = contentIds;
        this.destinationFolder = destinationFolder;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private List<String> contentIds;
        private String destinationFolder;

        /**
         * List of content ids which needs to export.
         */
        public Builder exportContents(List<String> contentIds) {
            this.contentIds = contentIds;
            return this;
        }

        /**
         * Absolute path of the destination folder.
         */
        public Builder toFolder(String toFolder) {
            if (StringUtil.isNullOrEmpty(toFolder)) {
                throw new IllegalArgumentException("Illegal toFolder, should not be null or empty.");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        public ContentExportRequest build() {
            if (StringUtil.isNullOrEmpty(destinationFolder)) {
                throw new IllegalStateException("To folder required.");
            }
            return new ContentExportRequest(contentIds, destinationFolder);
        }
    }
}
