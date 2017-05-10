package org.ekstep.genieservices.commons.db.core;

public interface IUpdatable {
    ContentValues getFieldsToUpdate();

    String getTableName();

    String updateBy();
}
