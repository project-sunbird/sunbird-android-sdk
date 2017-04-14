package org.ekstep.genieservices.commons.db.operations.impl;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.operations.IOperate;
import org.ekstep.genieservices.commons.db.operations.IReadDb;


public class CustomReader implements IOperate {

    private IReadDb model;
    private String query;

    public CustomReader(IReadDb model,String query) {
        this.model = model;
        this.query = query;
    }

    @Override
    public Void perform(SQLiteDatabase database) {
        Cursor result = database.rawQuery(query, null);
        model.read(result);
        return null;
    }

    @Override
    public SQLiteDatabase getConnection(ServiceDbHelper dbHelper) {
        return dbHelper.getReadableDatabase();
    }

    @Override
    public void beforePerform(Context context) {

    }
}
