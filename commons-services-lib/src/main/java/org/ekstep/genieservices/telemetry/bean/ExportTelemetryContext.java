package org.ekstep.genieservices.telemetry.bean;

import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Map;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExportTelemetryContext {

    private Map<String, Object> metadata;
    private IDBSession dbSession;   // External DB

    public ExportTelemetryContext(Map<String, Object> metadata) {
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
