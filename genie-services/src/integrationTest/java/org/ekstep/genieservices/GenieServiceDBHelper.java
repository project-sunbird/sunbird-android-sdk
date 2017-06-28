package org.ekstep.genieservices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.core.impl.SQLiteResultSet;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.telemetry.model.EventModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swayangjit on 11/5/17.
 */

public class GenieServiceDBHelper {
    private final static String TAG = GenieServiceDBHelper.class.getSimpleName();
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

    public static List<EventModel> findEventById(String eid) {
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(generateQuery(eid), null);
        List<EventModel> events = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst())
            do {
                EventModel eventModel = EventModel.build(sGSDBHelper.mAppContext.getDBSession());
                eventModel.readWithoutMoving(new SQLiteResultSet(cursor));
                events.add(eventModel);
            } while (cursor.moveToNext());
        cursor.close();

        return events;
    }

    public static ContentModel findContent(String identifier) {
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(findEcarDBEntry(identifier), null);
        List<EventModel> events = new ArrayList<>();
        ContentModel contentModel = ContentModel.build(sGSDBHelper.mAppContext.getDBSession());
        if (cursor != null && cursor.moveToFirst())
            do {

                contentModel.readWithoutMoving(new SQLiteResultSet(cursor));
            } while (cursor.moveToNext());
        cursor.close();

        return contentModel;
    }

    public static void clearProfileTable() {

        try {
            int count = getDatabase().delete("profiles", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "error deleting DB entries :: ");
        }

    }

    public static List<Profile> findProfile() {

        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(generateProfileQuery(), null);

        List<Profile> profiles = null;

        if (cursor != null && cursor.moveToFirst())
            do {
                profiles = new ArrayList<>();
                Profile profile = new Profile(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_UID)));

                profile.setUid(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_UID)));

                profile.setHandle(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_HANDLE)));

                profile.setAvatar(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AVATAR)));

                profile.setAge(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AGE)));

                profile.setGender(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GENDER)));

                profile.setStandard(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_STANDARD)));

                profile.setLanguage(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_LANGUAGE)));

                profile.setDay(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_DAY)));

                profile.setMonth(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MONTH)));

                boolean isGroupUser = cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_IS_GROUP_USER)) == 1;
                profile.setGroupUser(isGroupUser);

                profile.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_CREATED_AT))));

                profile.setMedium(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MEDIUM)));

                profile.setBoard(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_BOARD)));

                profiles.add(profile);

            } while (cursor.moveToNext());

        cursor.close();

        return profiles;
    }

    public static void clearTelemetryTableEntry() {
        try {
            int count = getDatabase().delete("telemetry", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

    }

    private static String generateProfileQuery() {
        return "SELECT * FROM profiles";
    }

    public static String generateQuery(String eid) {
        return "SELECT * FROM telemetry where event_type='" + eid + "'";
    }

    public static String findEcarDBEntry(String content_id) {
        Log.e(TAG, "findEcarDBEntry");
        return "SELECT * FROM content where identifier='" + content_id + "'";
    }

    public static void clearEcarEntryFromDB() {
        try {
            int count = getDatabase().delete("content", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "Unable to delete content DB entry");
        }
    }

    public static void clearPartnerDBEntry() {
        try {
            int count = getDatabase().delete("partners", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "Unable to delete partner DB entry");
        }
    }
}
