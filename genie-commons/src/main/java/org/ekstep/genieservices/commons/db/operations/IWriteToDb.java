package org.ekstep.genieservices.commons.db.operations;

import android.content.Context;

import org.ekstep.genieservices.commons.db.ContentValues;

public interface IWriteToDb {
    ContentValues getContentValues();

    void updateId(long id);

    String getTableName();

    void beforeWrite(Context context);
}
