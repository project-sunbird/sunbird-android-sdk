package org.ekstep.genieservices.commons.db.core;

import org.ekstep.genieservices.commons.db.core.impl.ContentValues;

public interface IUpdateDb {
    ContentValues getFieldsToUpdate();

    String getTableName();

    String updateBy();
}
