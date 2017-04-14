package org.ekstep.genieservices.commons.db.operations;

import android.content.ContentValues;
import android.content.Context;

public interface IWriteToDb {
    ContentValues getContentValues();
    void updateId(long id);
    String getTableName();
    void beforeWrite(Context context);
}
