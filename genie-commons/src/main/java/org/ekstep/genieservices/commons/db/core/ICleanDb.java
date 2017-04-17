package org.ekstep.genieservices.commons.db.core;

public interface ICleanDb {
    String getTableName();

    void clean();

    String selectionToClean();
}
