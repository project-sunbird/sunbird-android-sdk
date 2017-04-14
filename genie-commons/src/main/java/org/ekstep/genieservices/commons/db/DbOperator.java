package org.ekstep.genieservices.commons.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.ekstep.genieservices.commons.db.operations.IOperate;

import java.util.List;

public class DbOperator {

    private static final String LOG_TAG = DbOperator.class.getSimpleName();
    private ServiceDbHelper serviceDbHelper;
    private Context context;

    public DbOperator(Context context) {
        this(context, ServiceDbHelper.getGSDBInstance(context));
    }

    public DbOperator(Context context, SummarizerDBContext dbContext) {
        this(context, ServiceDbHelper.getSummarizerDBInstance(context));
    }

    public DbOperator(Context context, ServiceDbHelper serviceDbHelper) {
        this.context = context;
        this.serviceDbHelper = serviceDbHelper;
    }

    public Void executeInOneTransaction(List<IOperate> dbOperators){
        SQLiteDatabase writableDatabase = serviceDbHelper.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            for(IOperate operator:dbOperators){
                operator.beforePerform(context);
                operator.perform(writableDatabase);
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

    public Void execute(IOperate operate){
        SQLiteDatabase database = operate.getConnection(serviceDbHelper);
        try {
            operate.beforePerform(context);
            operate.perform(database);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error when performing execute. Exception: " + e, e);
        }
        finally {
//            database.close();
        }
        return null;
    }
}
