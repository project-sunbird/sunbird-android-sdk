package org.ekstep.genieservices.commons.db.core;

public interface IUpdateDb {
    ContentValues getFieldsToUpdate();

    String getTableName();

    String updateBy();
}
