package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * Created on 9/18/2017.
 *
 * @author anil
 */
public class ContentMoveRequest {

    private List<String> contentIds;
    private String destinationFolder;
//    private Step Duplicate/merge/

    public ContentMoveRequest(List<String> contentIds, String destinationFolder) {
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
         * List of content identifier which needs to move. If not set than by default will move all contents.
         */
        public Builder contentIds(List<String> contentIds) {
            this.contentIds = contentIds;
            return this;
        }

        /**
         * Destination folder where content will import.
         */
        public Builder toFolder(String toFolder) {
            if (StringUtil.isNullOrEmpty(toFolder)) {
                throw new IllegalArgumentException("Illegal toFolder");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        public ContentMoveRequest build() {
            if (StringUtil.isNullOrEmpty(destinationFolder)) {
                throw new IllegalStateException("To folder required.");
            }

            return new ContentMoveRequest(contentIds, destinationFolder);
        }
    }
}
