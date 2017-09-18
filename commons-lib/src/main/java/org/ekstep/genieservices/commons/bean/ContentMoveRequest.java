package org.ekstep.genieservices.commons.bean;

/**
 * Created on 9/18/2017.
 *
 * @author anil
 */
public class ContentMoveRequest {

    private String destinationFolder;

    public ContentMoveRequest(String destinationFolder) {
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
            if (toFolder == null) {
                throw new IllegalArgumentException("Illegal toFolder");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        public ContentMoveRequest build() {
            if (destinationFolder == null) {
                throw new IllegalStateException("To folder required.");
            }

            return new ContentMoveRequest(destinationFolder);
        }
    }
}
