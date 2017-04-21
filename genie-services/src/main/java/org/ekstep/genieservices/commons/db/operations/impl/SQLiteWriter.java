package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBOperation;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Locale;

/**
 * @author anil
 */
public class SQLiteWriter implements IDBOperation<SQLiteDatabase> {
    private static final String LOG_TAG = "service-SQLiteWriter";
    private IWritable model;

    public SQLiteWriter(IWritable model) {
        this.model = model;
    }

    @Override
    public Void perform(AppContext context, SQLiteDatabase datasource) {
        beforePerform(context);
        long id = datasource.insert(model.getTableName(), null, mapContentValues(model.getContentValues()));
        Logger.i(context, LOG_TAG, "Saving in db:" + model.getTableName());
        if (id != -1) {
            Logger.i(context, LOG_TAG, "Saved successfully in:" + model.getTableName() + " with id:" + id);
            model.updateId(id);
        } else {
            throw new DbException(String.format(Locale.US, "Failed to write to %s", model.getTableName()));
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

    private void beforePerform(AppContext context) {
        model.beforeWrite(context);
    }
}
