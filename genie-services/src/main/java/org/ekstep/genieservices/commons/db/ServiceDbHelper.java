package org.ekstep.genieservices.commons.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.IMigrate;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteSession;

import java.util.List;

/**
 * This class is a base class for SQLite DB.
 *
 * @author anil
 */
public class ServiceDbHelper extends SQLiteOpenHelper {

    private static ServiceDbHelper mGSDBInstance;
    private final List<Migration> migrations;
    private AppContext mAppContext;
    private IDBSessionHandler handler;

    private ServiceDbHelper(AppContext<Context> appContext, IDBContext dbContext) {
        // Use the application context, which will ensure that you don't accidentally leak an Activity's context.
        super(appContext.getContext().getApplicationContext(), dbContext.getDBName(), null, dbContext.getDBVersion());

        this.mAppContext = appContext;
        this.migrations = dbContext.getMigrations();
    }

    public static synchronized IDBSession getGSDBSession(AppContext<Context> appContext) {
        if (mGSDBInstance == null) {
            mGSDBInstance = new ServiceDbHelper(appContext, new GSDBContext());
            mGSDBInstance.handler = new IDBSessionHandler() {
                @Override
                public Void setDBSession(AppContext<Context> appContext, SQLiteDatabase database) {
                    appContext.setDBSession(new SQLiteSession(appContext, database));
                    return null;
                }
            };
        }
        return new SQLiteSession(appContext, mGSDBInstance.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Setting the dbsession so that the migrations can work. These callback methods get called
            when getWriteableDatabase is called the first time after install/upgrade and at that point
            the appContext will not contain the db session */
        handler.setDBSession(mAppContext, db);
        for (IMigrate migration : migrations) {
            migration.apply(mAppContext);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* Setting the dbsession so that the migrations can work. These callback methods get called
            when getWriteableDatabase is called the first time after install/upgrade and at that point
            the appContext will not contain the db session */
        handler.setDBSession(mAppContext, db);
        for (IMigrate migration : migrations) {
            if (migration.shouldBeApplied(oldVersion, newVersion)) {
                migration.apply(mAppContext);
            }
        }
    }

    interface IDBSessionHandler {
        Void setDBSession(AppContext<Context> appContext, SQLiteDatabase database);
    }
}
