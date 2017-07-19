package org.ekstep.genieservices.telemetry.bean;

import org.ekstep.genieservices.commons.db.operations.IDataSource;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExportTelemetryContext {

    private String destinationFolder;
    private IDataSource dataSource;
    private String sourceDBFilePath;
    private int sourceDBVersion;
    private String destinationDBFilePath;
    private Map<String, Object> metadata;

    public ExportTelemetryContext(String destinationFolder, IDataSource dataSource, String sourceDBFilePath, int sourceDBVersion) {
        this.destinationFolder = destinationFolder;
        this.dataSource = dataSource;
        this.sourceDBFilePath = sourceDBFilePath;
        this.sourceDBVersion = sourceDBVersion;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public IDataSource getDataSource() {
        return dataSource;
    }

    public String getSourceDBFilePath() {
        return sourceDBFilePath;
    }

    public int getSourceDBVersion() {
        return sourceDBVersion;
    }

    public String getDestinationDBFilePath() {
        return destinationDBFilePath;
    }

    public void setDestinationDBFilePath(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

}
