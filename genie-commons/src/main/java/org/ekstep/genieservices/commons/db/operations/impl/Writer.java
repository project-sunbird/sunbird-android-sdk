package org.ekstep.genieservices.commons.db.operations.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.operations.IOperate;
import org.ekstep.genieservices.commons.db.operations.IWriteToDb;
import org.ekstep.genieservices.commons.exception.DbException;

import java.util.Locale;

public class Writer implements IOperate {
    private final String LOG_TAG = "service-Writer";
    private IWriteToDb model;

    public Writer(IWriteToDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase database) {
        long id = database.insert(model.getTableName(), null, model.getContentValues());
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
    public void beforePerform(Context context) {
        model.beforeWrite(context);
    }
}
