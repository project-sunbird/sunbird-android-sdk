package org.ekstep.genieservices.commons.db.operations.impl;


import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IUpdateDb;
import org.ekstep.genieservices.commons.db.operations.IDBOperate;
import org.ekstep.genieservices.commons.exception.DbException;

import java.util.Locale;

public class SQLiteUpdater implements IDBOperate<SQLiteDatabase> {
    private IUpdateDb model;

    public SQLiteUpdater(IUpdateDb model) {
        this.model = model;
    }

    @Override
    public Void perform(AppContext context, SQLiteDatabase datasource) {
        int rowsCount = datasource.update(model.getTableName(), mapContentValues(model.getFieldsToUpdate()), model.updateBy(), null);
        if (rowsCount < 1) {
            throw new DbException(String.format(Locale.US, "Failed to update %s, for fields:%s, updated by: %s", model.getTableName(), model.getFieldsToUpdate(), model.updateBy()));
        }
        return null;
    }

    @NonNull
    private android.content.ContentValues mapContentValues(ContentValues values) {
        android.content.ContentValues contentValues = new android.content.ContentValues();

        if (values != null) {
            for (String colName : values.keySet()) {
                Object obj = values.get(colName);

                if (obj != null) {      // Cursor.FIELD_TYPE_NULL
                    if (obj instanceof byte[]) {     // Cursor.FIELD_TYPE_BLOB
                        contentValues.put(colName, (byte[]) obj);
                    } else if (obj instanceof Float || obj instanceof Double) {     // Cursor.FIELD_TYPE_FLOAT
                        contentValues.put(colName, ((Number) obj).doubleValue());
                    } else if (obj instanceof Long || obj instanceof Integer
                            || obj instanceof Short || obj instanceof Byte) {       // Cursor.FIELD_TYPE_INTEGER
                        contentValues.put(colName, ((Number) obj).longValue());
                    } else if (obj instanceof Boolean) {
                        contentValues.put(colName, ((Boolean) obj ? 1 : 0));
                    } else {    // Cursor.FIELD_TYPE_STRING
                        contentValues.put(colName, obj.toString());
                    }
                }
            }
        }

        return contentValues;
    }
}
