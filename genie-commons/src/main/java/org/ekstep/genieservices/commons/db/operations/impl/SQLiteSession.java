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
    private ServiceDbHelper serviceDbHelper;
    private AppContext appContext;

    public SQLiteSession(AppContext appContext) {
        this(appContext, ServiceDbHelper.getGSDBInstance(appContext.getContext()));
    }

    public SQLiteSession(AppContext appContext, SummarizerDBContext dbContext) {
        this(appContext, ServiceDbHelper.getSummarizerDBInstance(appContext.getContext()));
    }

    public SQLiteSession(AppContext appContext, ServiceDbHelper serviceDbHelper) {
        this.appContext = appContext;
        this.serviceDbHelper = serviceDbHelper;
    }

    @Override
    public Void execute(IOperate operate) {
//        SQLiteDatabase database = operate.getConnection(serviceDbHelper);
        try {
            operate.beforePerform(appContext);
            operate.perform(appContext);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error when performing execute. Exception: " + e, e);
        } finally {
//            database.close();
        }
        return null;
    }

    @Override
    public Void executeInOneTransaction(List<IOperate> dbOperators) {
        SQLiteDatabase writableDatabase = serviceDbHelper.getWritableDatabase();
        try {
            writableDatabase.beginTransaction();
            for (IOperate operator : dbOperators) {
                operator.beforePerform(appContext);
                operator.perform(appContext);
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error when performing execute in one transaction. Exception: " + e, e);
            throw e;
        } finally {
            writableDatabase.endTransaction();
//            writableDatabase.close();
        }
        return null;
    }

    @Override
    public ServiceDbHelper getDbHelper() {
        return serviceDbHelper;
    }

    @Override
    public IOperate getCleaner(ICleanDb cleanDb) {
        return new Cleaner(cleanDb);
    }

    @Override
    public IOperate getReader(IReadDb readDb) {
        return new Reader(readDb);
    }

    @Override
    public IOperate getWriter(IWriteToDb writeToDb) {
        return new Writer(writeToDb);
    }

    @Override
    public IOperate getUpdater(IUpdateDb updateDb) {
        return new Updater(updateDb);
    }

    @Override
    public IOperate getQueryExecutor(String query) {
        return new QueryExecutor(query);
    }

}
