package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This request class contains all the specifications that needs to be followed when switching the location
 * of the folder.
 */
public class ContentSwitchRequest {

    private String destinationFolder;

    private ContentSwitchRequest(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private String destinationFolder;

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

        public ContentSwitchRequest build() {
            if (StringUtil.isNullOrEmpty(destinationFolder)) {
                throw new IllegalStateException("To folder required.");
            }

            return new ContentSwitchRequest(destinationFolder);
        }
    }
}
