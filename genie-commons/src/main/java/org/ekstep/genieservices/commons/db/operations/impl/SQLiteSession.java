package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.SummarizerDBContext;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.IUpdateDb;
import org.ekstep.genieservices.commons.db.core.IWriteToDb;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IOperate;

import java.util.List;

public class SQLiteSession implements IDBSession {

    private static final String LOG_TAG = SQLiteSession.class.getSimpleName();
    private AppContext appContext;
    private SQLiteDatabase database;
    private boolean isOperationSuccessful;

    public SQLiteSession(AppContext appContext) {
        this(appContext, ServiceDbHelper.getGSDBInstance(appContext.getContext()));
    }

    public SQLiteSession(AppContext appContext, SummarizerDBContext dbContext) {
        this(appContext, ServiceDbHelper.getSummarizerDBInstance(appContext.getContext()));
    }

    private SQLiteSession(AppContext appContext, ServiceDbHelper serviceDbHelper) {
        this.appContext = appContext;
        this.database = serviceDbHelper.getWritableDatabase();
    }

    private Void execute(IOperate<SQLiteDatabase> operate) {
        try {
            operate.beforePerform(appContext);
            operate.perform(database);
        } catch (Exception e) {
            isOperationSuccessful = false;
            Log.e(LOG_TAG, "Error when performing execute. Exception: " + e, e);
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
    public Void clean(ICleanDb cleanDb) {
        return execute(new SQLiteCleaner(cleanDb));
    }

    @Override
    public Void read(IReadDb readDb) {
        return execute(new SQLiteReader(readDb));
    }

    @Override
    public Void create(IWriteToDb writeToDb) {
        return execute(new SQLiteWriter(writeToDb));
    }

    @Override
    public Void update(IUpdateDb updateDb) {
        return execute(new SQLiteUpdater(updateDb));
    }

    @Override
    public Void execute(String query) {
        return execute(new SQLiteQueryExecutor(query));
    }
}
