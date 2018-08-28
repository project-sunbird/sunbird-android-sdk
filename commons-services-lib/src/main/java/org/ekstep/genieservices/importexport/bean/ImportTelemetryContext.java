package org.ekstep.genieservices.importexport.bean;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportTelemetryContext {

    private String sourceDBFilePath;
    private Map<String, Object> metadata;

    public ImportTelemetryContext(String sourceDBFilePath) {
        this.sourceDBFilePath = sourceDBFilePath;
    }

    public String getSourceDBFilePath() {
        return sourceDBFilePath;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
