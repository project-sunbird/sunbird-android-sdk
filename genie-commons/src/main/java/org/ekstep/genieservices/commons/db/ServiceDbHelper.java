package org.ekstep.genieservices.commons.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is a base class for SQLITE DB.
 */
public class ServiceDbHelper extends SQLiteOpenHelper {
    //Please don't make any changes in the class
    private static ServiceDbHelper mGSDBInstance;
    private static ServiceDbHelper mSummarizerDBInstance;
//    private final List<Migration> migrations;
//    private BeforeMigrations beforeMigrations;

    private ServiceDbHelper(Context context) {
        this(context, new GSDBContext());
    }

    private ServiceDbHelper(Context context, DBContext dbContext) {
        super(context, dbContext.getDBName(), null, dbContext.getDBVersion());
//        this.migrations = dbContext.getMigrations();
//        this.beforeMigrations = dbContext.getMigrationIntroduced();
    }

    public static synchronized ServiceDbHelper getGSDBInstance(Context ctx) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mGSDBInstance == null) {
            mGSDBInstance = new ServiceDbHelper(ctx.getApplicationContext(), new GSDBContext());
        }
        return mGSDBInstance;
    }

    public static synchronized ServiceDbHelper getSummarizerDBInstance(Context ctx) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mSummarizerDBInstance == null) {
            mSummarizerDBInstance = new ServiceDbHelper(ctx.getApplicationContext(), new SummarizerDBContext());
        }
        return mSummarizerDBInstance;
    }

    // Use this instance only for test case
    public static ServiceDbHelper getTestInstance(Context ctx, DBContext dbContext) {
        ServiceDbHelper instance = new ServiceDbHelper(ctx.getApplicationContext(), dbContext);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
//        beforeMigrations.onCreate(db);
//        for (IMigrate migration : migrations)
//            migration.apply(db);
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
//        beforeMigrations.onUpgrade(db, oldVersion, newVersion);
//        for (IMigrate migration : migrations)
//            if (migration.shouldBeApplied(oldVersion, newVersion))
//                migration.apply(db);
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }
}
