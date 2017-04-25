package org.ekstep.genieservices.commons.db.operations.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.SummarizerDBContext;
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
    private AppContext appContext;
    private SQLiteDatabase database;
    private boolean isOperationSuccessful;

    public SQLiteSession(AppContext<Context, AndroidLogger> appContext) {
        this(appContext, ServiceDbHelper.getGSDBInstance(appContext));
    }

    public SQLiteSession(AppContext<Context, AndroidLogger> appContext, SummarizerDBContext dbContext) {
        this(appContext, ServiceDbHelper.getSummarizerDBInstance(appContext));
    }

    private SQLiteSession(AppContext<Context, AndroidLogger> appContext, ServiceDbHelper serviceDbHelper) {
        this.appContext = appContext;
        this.database = serviceDbHelper.getWritableDatabase();
    }

    private Void execute(IDBOperation<SQLiteDatabase> operate) {
        try {
            operate.perform(appContext, database);
        } catch (Exception e) {
            isOperationSuccessful = false;
            Logger.e(appContext, LOG_TAG, "Error when performing execute. Exception: " + e, e);
        }
        return null;
    }

    @Override
    public Void executeInTransaction(IDBTransaction transaction) {
        beginTransaction();
        try {
            transaction.perform(appContext);
            isOperationSuccessful = true;
        } catch (Exception e) {
            isOperationSuccessful = false;
            Logger.e(appContext, LOG_TAG, "Error when performing execute. Exception: " + e, e);
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
        return execute(new SQLiteReader(readDb, null));
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
