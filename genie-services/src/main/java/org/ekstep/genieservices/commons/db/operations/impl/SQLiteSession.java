package org.ekstep.genieservices.commons.db.operations.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBOperation;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.Logger;

public class SQLiteSession implements IDBSession {

    private static final String LOG_TAG = SQLiteSession.class.getSimpleName();
    private AppContext<Context> appContext;
    private SQLiteDatabase database;
    private boolean isOperationSuccessful;
    private String dbName;
    private int dbVersion;

    public SQLiteSession(AppContext<Context> appContext, SQLiteDatabase database, String dbName, int dbVersion) {
        this.appContext = appContext;
        this.database = database;
        this.dbName = dbName;
        this.dbVersion = dbVersion;
    }

    @Override
    public String getDBName() {
        return this.dbName;
    }

    @Override
    public int getDBVersion() {
        return dbVersion;
    }

    @Override
    public String getDatabasePath() {
        return appContext.getContext().getDatabasePath(dbName).getPath();
    }

    private Void execute(IDBOperation<SQLiteDatabase> operate) {
        try {
            operate.perform(appContext, database);
        } catch (Exception e) {
            isOperationSuccessful = false;
            Logger.e(LOG_TAG, "Error when performing execute. Exception: " + e, e);
        }
        return null;
    }

    @Override
    public Void executeInTransaction(IDBTransaction transaction) {
        beginTransaction();
        try {
            transaction.perform(this);
            isOperationSuccessful = true;
        } catch (Exception e) {
            isOperationSuccessful = false;
            Logger.e(LOG_TAG, "Error when performing execute. Exception: " + e, e);
        } finally {
            endTransaction();
        }
        return null;
    }

    @Override
    public Void beginTransaction() {
        isOperationSuccessful = true;
        database.beginTransaction();
        return null;
    }

    @Override
    public Void endTransaction() {
        if (isOperationSuccessful) {
            database.setTransactionSuccessful();
        }
        isOperationSuccessful = true;
        database.endTransaction();
        return null;
    }

    @Override
    public Void clean(ICleanable cleanDb) {
        return execute(new SQLiteCleaner(cleanDb));
    }

    @Override
    public Void read(IReadable readDb) {
        return execute(new SQLiteReader(readDb));
    }

    @Override
    public Void read(IReadable readDb, String customQuery) {
        return execute(new SQLiteReader(readDb, customQuery));
    }

    @Override
    public Void create(IWritable writeToDb) {
        return execute(new SQLiteWriter(writeToDb));
    }

    @Override
    public Void update(IUpdatable updateDb) {
        return execute(new SQLiteUpdater(updateDb));
    }

    @Override
    public Void execute(String query) {
        return execute(new SQLiteQueryExecutor(query));
    }

}
