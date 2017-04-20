package org.ekstep.genieservices.commons.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.IMigrate;
import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.List;

/**
 * This class is a base class for SQLite DB.
 *
 * @author anil
 */
public class ServiceDbHelper extends SQLiteOpenHelper {

    //Please don't make any changes in the class
    private static ServiceDbHelper mGSDBInstance;
    private static ServiceDbHelper mSummarizerDBInstance;
    private final List<Migration> migrations;
    private AppContext mAppContext;

    private ServiceDbHelper(AppContext<Context, AndroidLogger> appContext, IDBContext dbContext) {
        super(appContext.getContext().getApplicationContext(), dbContext.getDBName(), null, dbContext.getDBVersion());

        this.mAppContext = appContext;
        this.migrations = dbContext.getMigrations();
    }

    public static synchronized ServiceDbHelper getGSDBInstance(AppContext<Context, AndroidLogger> appContext) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mGSDBInstance == null) {
            mGSDBInstance = new ServiceDbHelper(appContext, new GSDBContext());
        }

        return mGSDBInstance;
    }

    public static synchronized ServiceDbHelper getSummarizerDBInstance(AppContext<Context, AndroidLogger> appContext) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mSummarizerDBInstance == null) {
            mSummarizerDBInstance = new ServiceDbHelper(appContext, new SummarizerDBContext());
        }

        return mSummarizerDBInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        for (IMigrate migration : migrations) {
            migration.apply(mAppContext);
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        for (IMigrate migration : migrations) {
            if (migration.shouldBeApplied(oldVersion, newVersion)) {
                migration.apply(mAppContext);
            }
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

}
