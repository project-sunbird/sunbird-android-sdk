package org.ekstep.genieservices.commons.db.operations.impl;


import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.operations.IOperate;

public class SQLiteQueryExecutor implements IOperate<SQLiteDatabase> {

    private String query;

    public SQLiteQueryExecutor(String query) {
        this.query = query;
    }

    @Override
    public Void perform(SQLiteDatabase datasource) {
        datasource.execSQL(query);
        return null;
    }

    @Override
    public void beforePerform(AppContext context) {

    }
}
