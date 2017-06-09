package org.ekstep.genieservices.commons.db.operations.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDataSource;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class SQLiteDataSource implements IDataSource {

    private AppContext<Context> appContext;

    public SQLiteDataSource(AppContext<Context> appContext) {
        this.appContext = appContext;
    }

    @Override
    public IDBSession getImportDataSource(String filePath) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        return new SQLiteSession(appContext, database);
    }
}
