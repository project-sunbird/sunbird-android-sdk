package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 6/12/2017.
 *
 * @author anil
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

        public Builder toFolder(String destinationFolder) {
            this.destinationFolder = destinationFolder;
            return this;
        }

        public ContentExportRequest build() {
            return new ContentExportRequest(contentIds, destinationFolder);
        }
    }
}
