package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.impl.SQLiteResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBOperation;

import java.util.Locale;

public class SQLiteReader implements IDBOperation<SQLiteDatabase> {

    private IReadable model;
    private String customQuery;

    public SQLiteReader(IReadable model) {
        this.model = model;
    }

    public SQLiteReader(IReadable model, String customQuery) {
        this.model = model;
        this.customQuery = customQuery;
    }

    @Override
    public Void perform(AppContext context, SQLiteDatabase datasource) {
        String query = customQuery == null ? String.format(Locale.US, "Select * from %s %s %s %s", model.getTableName(),
                model.filterForRead(), model.orderBy(), model.limitBy()) : customQuery;

        Cursor cursor = datasource.rawQuery(query, model.selectionArgsForFilter());
        model.read(new SQLiteResultSet(cursor));
        cursor.close();
        return null;
    }

}
