package org.ekstep.genieservices.importexport.bean;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExportTelemetryContext {

    private String destinationFolder;
    private String destinationDBFilePath;
    private Map<String, Object> metadata;

    public ExportTelemetryContext(String destinationFolder, String destinationDBFilePath) {
        this.destinationFolder = destinationFolder;
        this.destinationDBFilePath = destinationDBFilePath;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public String getDestinationDBFilePath() {
        return destinationDBFilePath;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

}
