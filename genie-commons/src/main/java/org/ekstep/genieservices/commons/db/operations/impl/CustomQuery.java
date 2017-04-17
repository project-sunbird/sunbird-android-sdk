package org.ekstep.genieservices.commons.db.operations.impl;


import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.operations.IOperate;

public class CustomQuery implements IOperate {

    private String query;

    public CustomQuery(String query) {
        this.query = query;
    }

    @Override
    public Void perform(SQLiteDatabase db) {
        db.execSQL(query);
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
