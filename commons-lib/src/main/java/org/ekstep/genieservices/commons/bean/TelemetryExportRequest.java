package org.ekstep.genieservices.commons.bean;

/**
 * Created on 6/12/2017.
 *
 * @author anil
 */
public class TelemetryExportRequest {

    private String destinationFolder;

    private TelemetryExportRequest(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public static class Builder {
        private String destinationFolder;

        public Builder toFolder(String destinationFolder) {
            this.destinationFolder = destinationFolder;
            return this;
        }

        public TelemetryExportRequest build() {
            return new TelemetryExportRequest(destinationFolder);
        }
    }
}
