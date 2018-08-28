package org.ekstep.genieservices.commons.db.operations.impl;


import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.operations.IDBOperation;

public class SQLiteQueryExecutor implements IDBOperation<SQLiteDatabase> {

    private String query;

    public SQLiteQueryExecutor(String query) {
        this.query = query;
    }

    @Override
    public Void perform(AppContext context, SQLiteDatabase datasource) {
        datasource.execSQL(query);
        return null;
    }
}
