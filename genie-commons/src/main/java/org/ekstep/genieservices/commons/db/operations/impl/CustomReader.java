package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.impl.SqliteResultSet;
import org.ekstep.genieservices.commons.db.operations.IOperate;

public class CustomReader implements IOperate {

    private IReadDb model;
    private String query;

    public CustomReader(IReadDb model, String query) {
        this.model = model;
        this.query = query;
    }

    @Override
    public Void perform(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery(query, null);
        model.read(new SqliteResultSet(cursor));
        return null;
    }

    @Override
    public SQLiteDatabase getConnection(ServiceDbHelper dbHelper) {
        return dbHelper.getReadableDatabase();
    }

    @Override
    public void beforePerform(AppContext context) {

    }

}
