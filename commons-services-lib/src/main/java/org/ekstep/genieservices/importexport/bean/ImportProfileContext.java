package org.ekstep.genieservices.importexport.bean;

import org.ekstep.genieservices.commons.db.operations.IDataSource;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportProfileContext {

    private IDataSource dataSource;
    private String sourceDBFilePath;
    private Map<String, Object> metadata;

    private int imported;
    private int failed;

    public ImportProfileContext(IDataSource dataSource, String sourceDBFilePath) {
        this.dataSource = dataSource;
        this.sourceDBFilePath = sourceDBFilePath;
    }

    public IDataSource getDataSource() {
        return dataSource;
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

    public int getImported() {
        return imported;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
}
