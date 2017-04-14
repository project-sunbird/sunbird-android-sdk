package org.ekstep.genieservices.commons.db.operations;


import android.content.ContentValues;

public interface IUpdateDb {
    ContentValues getFieldsToUpdate();
    String getTableName();
    String updateBy();
}
