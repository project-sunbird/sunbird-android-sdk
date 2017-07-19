package org.ekstep.genieservices.telemetry.bean;

import org.ekstep.genieservices.commons.db.operations.IDataSource;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportTelemetryContext {

    private IDataSource dataSource;
    private String sourceFilePath;
    private Map<String, Object> metadata;

    public ImportTelemetryContext(IDataSource dataSource, String sourceFilePath) {
        this.dataSource = dataSource;
        this.sourceFilePath = sourceFilePath;
    }

    public IDataSource getDataSource() {
        return dataSource;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
