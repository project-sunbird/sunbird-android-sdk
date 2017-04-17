package org.ekstep.genieservices.commons.db.operations.impl;


import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.operations.IOperate;

public class QueryExecutor implements IOperate {

    private String query;

    public QueryExecutor(String query) {
        this.query = query;
    }

    @Override
    public Void perform(AppContext appContext) {
        SQLiteDatabase database = appContext.getDBSession().getDbHelper().getWritableDatabase();

        database.execSQL(query);

        return null;
    }

    @Override
    public void beforePerform(AppContext context) {

    }
}
