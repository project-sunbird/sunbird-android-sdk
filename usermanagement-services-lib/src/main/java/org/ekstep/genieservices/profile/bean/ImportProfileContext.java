package org.ekstep.genieservices.profile.bean;

import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ImportProfileContext {

    private Map<String, Object> metadata;
    private IDBSession dbSession;   // External DB

    private int imported;
    private int failed;

    // Used for import/export
    public ImportProfileContext(IDBSession dbSession, Map<String, Object> metadata) {
        this.dbSession = dbSession;
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public IDBSession getDBSession() {
        return dbSession;
    }

    public void setDbSession(IDBSession dbSession) {
        this.dbSession = dbSession;
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
