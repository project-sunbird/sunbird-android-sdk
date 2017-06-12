package org.ekstep.genieservices.commons.bean;

import java.io.File;
import java.util.List;

/**
 * Created on 6/12/2017.
 *
 * @author anil
 */
public class ProfileExportRequest {

    private List<String> userIds;
    private File destinationFolder;

    private ProfileExportRequest(List<String> userIds, File destinationFolder) {
        this.userIds = userIds;
        this.destinationFolder = destinationFolder;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private List<String> userIds;
        private File destinationFolder;

        /**
         * List of user which needs to export.
         */
        public Builder exportUsers(List<String> userIds) {
            this.userIds = userIds;
            return this;
        }

        public Builder toFolder(File destinationFolder) {
            this.destinationFolder = destinationFolder;
            return this;
        }

        public ProfileExportRequest build() {
            return new ProfileExportRequest(userIds, destinationFolder);
        }
    }
}
