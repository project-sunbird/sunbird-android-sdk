package org.ekstep.genieservices.importexport.bean;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportProfileContext {

    private String sourceDBFilePath;
    private Map<String, Object> metadata;

    private int imported;
    private int failed;

    public ImportProfileContext(String sourceDBFilePath) {
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
