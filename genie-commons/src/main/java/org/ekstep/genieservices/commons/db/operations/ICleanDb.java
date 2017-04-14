package org.ekstep.genieservices.commons.db.operations;

public interface ICleanDb {
    String getTableName();
    void clean();
    String selectionToClean();
}
