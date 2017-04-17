package org.ekstep.genieservices.commons.db.operations.impl;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.operations.IOperate;
import org.ekstep.genieservices.commons.db.operations.IUpdateDb;
import org.ekstep.genieservices.commons.exception.DbException;

import java.util.Locale;

public class Updater implements IOperate {
    private IUpdateDb model;

    public Updater(IUpdateDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase db) {
        int rowsCount = db.update(model.getTableName(), model.getFieldsToUpdate(), model.updateBy(), null);
        if (rowsCount < 1) {
            throw new DbException(String.format(Locale.US, "Failed to update %s, for fields:%s, updated by: %s", model.getTableName(), model.getFieldsToUpdate(), model.updateBy()));
        }
        return null;
    }

    @Override
    public SQLiteDatabase getConnection(ServiceDbHelper dbHelper) {
        return dbHelper.getWritableDatabase();
    }

    @Override
    public void beforePerform(Context context) {

    }
}
