package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.operations.IOperate;

import java.util.Locale;

public class Cleaner implements IOperate {

    private ICleanDb model;

    public Cleaner(ICleanDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase database) {
        String query = String.format(Locale.US, "DELETE FROM %s %s", model.getTableName(), model.selectionToClean());
        database.execSQL(query);
        model.clean();
        return null;
    }

    @Override
    public SQLiteDatabase getConnection(ServiceDbHelper dbHelper) {
        return dbHelper.getWritableDatabase();
    }


    @Override
    public void beforePerform(AppContext context) {

    }
}
