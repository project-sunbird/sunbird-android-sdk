package org.ekstep.genieservices.commons.bean;

/**
 * This class accepts the desinationFolder name when requesting for the telemetry request.
 *
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
