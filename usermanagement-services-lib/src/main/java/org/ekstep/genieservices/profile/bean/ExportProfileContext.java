package org.ekstep.genieservices.profile.bean;

import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExportProfileContext {

    private Map<String, Object> metadata;
    private IDBSession dbSession;   // External DB

    public ExportProfileContext(IDBSession dbSession, Map<String, Object> metadata) {
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

}
