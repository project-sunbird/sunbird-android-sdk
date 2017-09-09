package org.ekstep.genieservices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.core.impl.SQLiteResultSet;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.content.db.model.ContentListingModel;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.profile.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.model.LearnerSummaryModel;
import org.ekstep.genieservices.telemetry.model.EventModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swayangjit on 11/5/17.
 */

public class GenieServiceDBHelper {
    private final static String TAG = GenieServiceDBHelper.class.getSimpleName();
    public static String GS_DB = "/data/data/org.ekstep.genieservices1/databases/GenieServices.db";
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

    public static List<EventModel> findAllEvents() {
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery("SELECT * FROM telemetry", null);
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

    public static List<ProcessedEventModel> findProcessedEvents() {
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery("SELECT * FROM processed_telemetry", null);
        List<ProcessedEventModel> events = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst())
            do {
                ProcessedEventModel eventModel = ProcessedEventModel.build(sGSDBHelper.mAppContext.getDBSession());
                eventModel.readWithoutMoving(new SQLiteResultSet(cursor));
                events.add(eventModel);
            } while (cursor.moveToNext());
        cursor.close();

        return events;
    }

    public static ContentModel findContentEntryInDB(String identifier) {
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

    public static List<LearnerAssessmentDetails> findLearnerAssesmentDetails() {

        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery("SELECT * FROM learner_assessments", null);
        List<LearnerAssessmentDetails> learnerAssessmentDetailsList = null;
        if (cursor != null && cursor.moveToFirst())
            do {
                learnerAssessmentDetailsList = new ArrayList<>();
                LearnerAssessmentDetails learnerAssessmentDetails = new LearnerAssessmentDetails();

                learnerAssessmentDetails.setUid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_UID)));

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID) != -1) {
                    learnerAssessmentDetails.setContentId(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID) != -1) {
                    learnerAssessmentDetails.setQid(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_QID)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_INDEX) != -1) {
                    learnerAssessmentDetails.setQindex(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT) != -1) {
                    learnerAssessmentDetails.setCorrect(cursor.getInt(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_CORRECT)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE) != -1) {
                    learnerAssessmentDetails.setScore(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_SCORE)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT) != -1) {
                    learnerAssessmentDetails.setTimespent(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIME_SPENT)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES) != -1) {
                    learnerAssessmentDetails.setRes(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_RES)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP) != -1) {
                    learnerAssessmentDetails.setTimestamp(cursor.getLong(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_TIMESTAMP)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC) != -1) {
                    learnerAssessmentDetails.setQdesc(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_DESC)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE) != -1) {
                    learnerAssessmentDetails.setQtitle(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_Q_TITLE)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA) != -1) {
                    learnerAssessmentDetails.setHierarchyData(cursor.getString(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA)));
                }

                if (cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE) != -1) {
                    learnerAssessmentDetails.setMaxScore(cursor.getDouble(cursor.getColumnIndex(LearnerAssessmentsEntry.COLUMN_NAME_MAX_SCORE)));
                }

                learnerAssessmentDetailsList.add(learnerAssessmentDetails);
            } while (cursor.moveToNext());

        cursor.close();


        return learnerAssessmentDetailsList;
    }

    public static List<LearnerSummaryModel> findLearnerContentSummaryDetails(String contentId) {

        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery("SELECT * FROM learner_content_summary where content_id='" + contentId + "'", null);
        List<LearnerSummaryModel> learnerSummaryModelList = null;
        if (cursor != null && cursor.moveToFirst()) {
            learnerSummaryModelList = new ArrayList<>();

            do {
                LearnerSummaryModel learnerSummaryModel = LearnerSummaryModel.build(sGSDBHelper.mAppContext.getDBSession());
                learnerSummaryModel.readWithoutMoving(new SQLiteResultSet(cursor));
                learnerSummaryModelList.add(learnerSummaryModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return learnerSummaryModelList;
    }


    public static ContentAccessModel findContentAccessInDB(String identifier) {
        String query = "SELECT * FROM " + ContentAccessEntry.TABLE_NAME + " where identifier='" + identifier + "'";
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(query, null);
        ContentAccessModel contentModel = ContentAccessModel.build(sGSDBHelper.mAppContext.getDBSession());
        if (cursor != null && cursor.moveToFirst())
            do {

                contentModel.readWithoutMoving(new SQLiteResultSet(cursor));
            } while (cursor.moveToNext());
        cursor.close();

        return contentModel;
    }

    public static void updateTtlinContentListingTable(ContentListingCriteria criteria) {
        ContentListingModel model = ContentListingModel.build(sGSDBHelper.mAppContext.getDBSession(), criteria, SampleApiResponse.getContentListingAPIResponse(), DateUtil.getEpochTime() - (2 * DateUtil.MILLISECONDS_IN_AN_HOUR));
        model.update();
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

    public static void clearContentEntryFromDB() {
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

    public static void clearUserTableDBEntry() {
        try {
            int count = getDatabase().delete("users", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "Unable to delete users DB entry");
        }
    }

    public static void clearTable(String tableName) {
        try {
            int count = getDatabase().delete(tableName, "1", null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}
