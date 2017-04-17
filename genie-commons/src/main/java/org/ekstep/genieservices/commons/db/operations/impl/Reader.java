package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.impl.SqliteResultSet;
import org.ekstep.genieservices.commons.db.operations.IOperate;

import java.util.Locale;

public class Reader implements IOperate {

    private IReadDb model;

    public Reader(IReadDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase database) {
        String query = String.format(Locale.US, "Select * from %s %s %s %s", model.getTableName(),
                model.filterForRead(), model.orderBy(), model.limitBy());
        Cursor cursor = database.rawQuery(query, model.selectionArgsForFilter());
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
