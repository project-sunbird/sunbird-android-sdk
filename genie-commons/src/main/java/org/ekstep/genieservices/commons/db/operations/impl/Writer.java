package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ContentValues;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.operations.IOperate;
import org.ekstep.genieservices.commons.db.operations.IWriteToDb;
import org.ekstep.genieservices.commons.exception.DbException;

import java.util.Locale;

/**
 * @author anil
 */
public class Writer implements IOperate {
    private static final String LOG_TAG = "service-Writer";
    private IWriteToDb model;

    public Writer(IWriteToDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase database) {

        android.content.ContentValues contentValues = new android.content.ContentValues();

        ContentValues values = model.getContentValues();
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

        long id = database.insert(model.getTableName(), null, contentValues);
        Log.i(LOG_TAG, "Saving in db:" + model.getTableName());
        if (id != -1) {
            Log.i(LOG_TAG, "Saved successfully in:" + model.getTableName() + " with id:" + id);
            model.updateId(id);
        } else {
            throw new DbException(String.format(Locale.US, "Failed to write to %s", model.getTableName()));
        }

        return null;
    }

    @Override
    public SQLiteDatabase getConnection(ServiceDbHelper dbHelper) {
        return dbHelper.getWritableDatabase();
    }

    @Override
    public void beforePerform(AppContext context) {
        model.beforeWrite(context);
    }
}
