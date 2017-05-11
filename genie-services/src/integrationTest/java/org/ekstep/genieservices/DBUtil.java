package org.ekstep.genieservices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.ekstep.genieservices.telemetry.model.EventsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 11/5/17.
 */

public class DBUtil {
    public static String GS_DB = "/data/data/org.ekstep.genieservices/databases/GenieServices.db";
    private static SQLiteDatabase db = null;
    public static SQLiteDatabase getInstance() {
        if (db == null) {

            db = SQLiteDatabase.openDatabase(GS_DB, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return db;
    }

//    public static EventModel findEvent(String eid, String addQuery) {
//        Cursor telemetryCursor = DBUtil.getInstance().rawQuery(generateQuery(eid, addQuery), null);
//        readAllEvents(telemetryCursor);
//        telemetryCursor.close();
//
//        return null;
//    }
////
//    public static List<EventModel> readAllEvents(Cursor cursor) {
//        List<EventModel> events = new ArrayList<>();
//        if (cursor != null && cursor.moveToFirst())
//            do {
//                events.add(EventModel.fi(cursor));
//            } while (cursor.moveToNext());
//        return events;
//    }

    public static String generateQuery(String eid, String addQuery) {
        return "SELECT * FROM telemetry  where event_type='" + eid + "' ORDER BY _id" + addQuery;
    }
}
