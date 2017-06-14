package org.ekstep.genieservices.commons.bean;

import java.io.File;

/**
 * Created on 6/12/2017.
 *
 * @author anil
 */
public class TelemetryExportRequest {

    private File destinationFolder;

    private TelemetryExportRequest(File destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private File destinationFolder;

        public Builder toFolder(File destinationFolder) {
            this.destinationFolder = destinationFolder;
            return this;
        }

        public TelemetryExportRequest build() {
            return new TelemetryExportRequest(destinationFolder);
        }
    }
}
