package org.ekstep.genieservices.commons.db.core;

public interface ICleanable {
    String getTableName();

    void clean();

    String selectionToClean();
}
