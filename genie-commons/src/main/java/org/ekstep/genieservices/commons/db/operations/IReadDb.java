package org.ekstep.genieservices.commons.db.operations;

import android.database.Cursor;

public interface IReadDb {
    IReadDb read(Cursor cursor);
    String getTableName();
    String orderBy();
    String filterForRead();
    String[] selectionArgsForFilter();
    String limitBy();
}
