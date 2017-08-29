package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class accepts userIds as {@link List<String>}, and destinationFolder when requesting to export user profiles.
 *
 */
public class ProfileExportRequest {

    private List<String> userIds;
    private String destinationFolder;

    private ProfileExportRequest(List<String> userIds, String destinationFolder) {
        this.userIds = userIds;
        this.destinationFolder = destinationFolder;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private List<String> userIds;
        private String destinationFolder;

        /**
         * List of user which needs to export.
         */
        public Builder exportUsers(List<String> userIds) {
            this.userIds = userIds;
            return this;
        }

        /**
         * Absolute path of destination folder where content will import.
         */
        public Builder toFolder(String destinationFolder) {
            this.destinationFolder = destinationFolder;
            return this;
        }

        public ProfileExportRequest build() {
            return new ProfileExportRequest(userIds, destinationFolder);
        }
    }
}
