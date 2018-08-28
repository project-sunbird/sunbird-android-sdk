package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * This class accepts the destinationFolder name when requesting for the telemetry request.
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

        /**
         * Absolute path of destination folder where telemetry will be exported.
         */
        public Builder toFolder(String toFolder) {
            if (StringUtil.isNullOrEmpty(toFolder)) {
                throw new IllegalArgumentException("Illegal toFolder, should not be null or empty.");
            }
            this.destinationFolder = toFolder;
            return this;
        }

        public TelemetryExportRequest build() {
            if (StringUtil.isNullOrEmpty(destinationFolder)) {
                throw new IllegalStateException("To folder required.");
            }
            return new TelemetryExportRequest(destinationFolder);
        }
    }
}
