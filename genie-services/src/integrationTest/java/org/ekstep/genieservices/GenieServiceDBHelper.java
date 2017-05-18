package org.ekstep.genieservices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.impl.SQLiteResultSet;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 11/5/17.
 */

public class GenieServiceDBHelper {
    public static String GS_DB = "/data/data/org.ekstep.genieservices.test/databases/GenieServices.db";
    private static SQLiteDatabase sSqliteDatabase = null;
    private static GenieServiceDBHelper sGSDBHelper;
    private AppContext mAppContext;

    private GenieServiceDBHelper(AppContext appContext) {
        this.mAppContext = appContext;
    }

    public static void init(AppContext appContext) {
        if (sGSDBHelper == null) {
            sGSDBHelper = new GenieServiceDBHelper(appContext);
        }
    }

    public static SQLiteDatabase getDatabase() {
        if (sSqliteDatabase == null) {
            sSqliteDatabase = SQLiteDatabase.openDatabase(GS_DB, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return sSqliteDatabase;
    }

//    public static List<EventModel> findEventById(String eid) {
//        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(generateQuery(eid), null);
//        List<EventModel> events = new ArrayList<>();
//        if (cursor != null && cursor.moveToFirst())
//            do {
//                events.add(EventModel.build(sGSDBHelper.mAppContext.getDBSession(),new SQLiteResultSet(cursor)));
//            } while (cursor.moveToNext());
//        cursor.close();
//
//        return null;
//    }

    public static String generateQuery(String eid) {
        return "SELECT * FROM telemetry  where event_type='" + eid + "'";
    }
}
