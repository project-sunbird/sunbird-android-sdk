package org.ekstep.genieservices;

import android.content.Context;
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
    public static String GS_DB = "";
    private static SQLiteDatabase sSqliteDatabase = null;
    private static GenieServiceDBHelper sGSDBHelper;
    private AppContext mAppContext;


    private GenieServiceDBHelper(AppContext appContext) {
        this.mAppContext = appContext;
        GS_DB = ((Context) (mAppContext.getContext())).getDatabasePath("GenieServices.db").getPath();
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

    public static ContentModel getContentPathInDB(String identifier) {
        String query = "SELECT * FROM content where identifier='" + identifier + "'";
        Cursor cursor = GenieServiceDBHelper.getDatabase().rawQuery(query, null);
        ContentModel contentModel = ContentModel.build(sGSDBHelper.mAppContext.getDBSession());
        if (cursor != null && cursor.moveToFirst())
            do {
                contentModel.readWithoutMoving(new SQLiteResultSet(cursor));
            } while (cursor.moveToNext());
        cursor.close();

        return contentModel;
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
        ContentListingModel model = ContentListingModel.build(sGSDBHelper.mAppContext.getDBSession(), criteria, getContentListingAPIResponse(), DateUtil.getEpochTime() - (2 * DateUtil.MILLISECONDS_IN_AN_HOUR));
        model.update();
    }

    public static String getContentListingAPIResponse() {
        return "{\n" +
                "  \"id\": \"ekstep.genie.content.home\",\n" +
                "  \"ver\": \"1.0\",\n" +
                "  \"ts\": \"2017-09-08T05:22:15+00:00\",\n" +
                "  \"params\": {\n" +
                "    \"resmsgid\": \"ef334316da79d18928fbc168d43f1491e541e0d2\",\n" +
                "    \"msgid\": \"\",\n" +
                "    \"status\": \"successful\",\n" +
                "    \"err\": \"\",\n" +
                "    \"errmsg\": \"\"\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"page\": {\n" +
                "      \"id\": \"org.ekstep.genie.content.home\",\n" +
                "      \"banners\": null,\n" +
                "      \"sections\": [\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Recommended\",\n" +
                "              \"hn\": \"सलाह\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": null,\n" +
                "          \"recommend\": {\n" +
                "            \"context\": {\n" +
                "              \"contentid\": \"\",\n" +
                "              \"did\": \"0b6db2c8e059cbe44ecb36d03c2c2908537eb019\",\n" +
                "              \"uid\": \"f3456746-e23d-495f-8b35-3674355be2a8\"\n" +
                "            },\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": null,\n" +
                "            \"filters\": null,\n" +
                "            \"facets\": null\n" +
                "          },\n" +
                "          \"contents\": null,\n" +
                "          \"resmsgid\": \"e7105789-b7b9-4624-91a3-47e8d881a2ca\",\n" +
                "          \"apiid\": \"ekstep.analytics.recommendations\",\n" +
                "          \"filterModifiable\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Collections\",\n" +
                "              \"hn\": \"सबसे लोकप्रिय\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Collection\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"IL_FUNC_OBJECT_TYPE\": \"Content\",\n" +
                "              \"IL_SYS_NODE_TYPE\": \"DATA_NODE\",\n" +
                "              \"IL_UNIQUE_ID\": \"do_2122432102803783681180\",\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-08T15:10:29.648+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"\\u003c5\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432102803783681180/artifact/eff35f43d89c00c409aae3dcbeed0c96_1494533052420.thumb.jpeg\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.6216\",\n" +
                "              \"collections\": [\n" +
                "                \"do_212270426641924096119\",\n" +
                "                \"do_21226836960507494418\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Collection\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"705\",\n" +
                "              \"createdFor\": [\n" +
                "                \"\"\n" +
                "              ],\n" +
                "              \"createdOn\": \"2017-05-12T11:53:21.644+0000\",\n" +
                "              \"creator\": \"Debesh Rout\",\n" +
                "              \"description\": \"Alphabets in Tamil for kids\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018449_do_2122432102803783681180_2.0.ecar\",\n" +
                "              \"es_metadata_id\": \"do_2122432102803783681180\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_2122432102803783681180\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedBy\": \"582\",\n" +
                "              \"lastPublishedOn\": \"2017-05-15T07:23:38.447+0000\",\n" +
                "              \"lastUpdatedBy\": \"582\",\n" +
                "              \"lastUpdatedOn\": \"2017-06-04T16:20:12.017+0000\",\n" +
                "              \"me_averageRating\": 5,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDownloads\": 74,\n" +
                "              \"me_totalRatings\": 1,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"mediaType\": \"collection\",\n" +
                "              \"medium\": \"Tamil\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.content-collection\",\n" +
                "              \"name\": \"தமிழ் எழுத்துக்கள் / Tamil alphabets\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"organization\": [\n" +
                "                \"\"\n" +
                "              ],\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debesh\",\n" +
                "              \"pkgVersion\": 2,\n" +
                "              \"portalOwner\": \"705\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432147653017601185/artifact/eff35f43d89c00c409aae3dcbeed0c96_1494533052420.jpeg\",\n" +
                "              \"prevState\": \"Review\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018449_do_2122432102803783681180_2.0.ecar\",\n" +
                "              \"screenshots\": [\n" +
                "                \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_2122432134616023041184/artifact/tamil4_705_1494532917_1494532893575.jpg\"\n" +
                "              ],\n" +
                "              \"size\": 72779,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"subject\": \"Tamil\",\n" +
                "              \"template\": \"\",\n" +
                "              \"usesContent\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"variants\": \"{\\\"spine\\\":{\\\"ecarUrl\\\":\\\"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_2122432102803783681180/tmilll-ellluttukkll-tamil-alphabets_1494833018577_do_2122432102803783681180_2.0_spine.ecar\\\",\\\"size\\\":72781.0}}\",\n" +
                "              \"versionKey\": \"1496934629648\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:25:57.356+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/do_20051999/artifact/10_1466502411254.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.collection.2850\",\n" +
                "              \"collections\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Collection\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"239\",\n" +
                "              \"createdOn\": \"2016-11-06T05:44:10.869+0000\",\n" +
                "              \"creator\": \"External Testing vendor\",\n" +
                "              \"description\": \"test\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_20051999/new-6-nov_1478411092834_do_20051999_1.0.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20051999\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 1\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20051999\",\n" +
                "              \"language\": [\n" +
                "                \"Marathi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-11-06T05:44:52.965+0000\",\n" +
                "              \"lastUpdatedBy\": \"239\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:08:55.110+0000\",\n" +
                "              \"mediaType\": \"collection\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.content-collection\",\n" +
                "              \"name\": \"new 6 nov\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 46030,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"External Testing vendor\",\n" +
                "              \"pkgVersion\": 1,\n" +
                "              \"portalOwner\": \"239\",\n" +
                "              \"posterImage\": \"https://qa.ekstep.in/assets/public/content/10_1466502411254.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_20051999/new-6-nov_1478411092834_do_20051999_1.0.ecar\",\n" +
                "              \"size\": 116757,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497245157356\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"5b3a32b5-4198-4c05-b93c-681415c2d0bf\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Worksheets\",\n" +
                "              \"hn\": \"सबसे नए\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"lastPublishedOn\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Worksheet\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:12:10.904+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1467721835076_do_30030595.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.numeracy.worksheet.674\",\n" +
                "              \"collections\": [\n" +
                "                \"do_2123165037383761921419\",\n" +
                "                \"do_2123213167671296001794\",\n" +
                "                \"do_2123213574735462401828\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"Num:C2:SC3:M8\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Worksheet\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"286\",\n" +
                "              \"createdOn\": \"2016-07-01T11:50:16.641+0000\",\n" +
                "              \"creator\": \"Harish S C\",\n" +
                "              \"description\": \"Pratham Barahkadi wifi123\",\n" +
                "              \"domain\": [\n" +
                "                \"numeracy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30030595_1467721847723.ecar\",\n" +
                "              \"es_metadata_id\": \"do_30030595\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_30030595\",\n" +
                "              \"imageCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"language\": [\n" +
                "                \"Hindi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-07-05T12:30:48.536+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-07-01T11:57:41.796+0000\",\n" +
                "              \"lastUpdatedBy\": \"286\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:04:07.699+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 0,\n" +
                "              \"me_averageInteractionsPerMin\": 22.37,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 3.27,\n" +
                "              \"me_averageTimespentPerSession\": 135.9,\n" +
                "              \"me_imagesCount\": 26,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 22,\n" +
                "              \"me_totalDownloads\": 2,\n" +
                "              \"me_totalInteractions\": 3648,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 72,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 9784.73,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"PC Barahkhadi\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"optStatus\": \"Complete\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Harish S C\",\n" +
                "              \"pkgVersion\": 7,\n" +
                "              \"popularity\": 8329.56,\n" +
                "              \"portalOwner\": \"286\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_30030595_1467721847723.ecar\",\n" +
                "              \"size\": 6.962254e+06,\n" +
                "              \"soundCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497244330904\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:06:37.987+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"6-7\",\n" +
                "                \"7-8\",\n" +
                "                \"8-10\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/division_1466401982132.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/1467630869246_do_30013040.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.numeracy.worksheet.469\",\n" +
                "              \"collections\": [\n" +
                "                \"do_2122981517191495681172\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Worksheet\",\n" +
                "              \"copyright\": \"CC0\",\n" +
                "              \"createdBy\": \"334\",\n" +
                "              \"createdOn\": \"2016-06-20T05:49:54.016+0000\",\n" +
                "              \"creator\": \"Parabal Partap Singh\",\n" +
                "              \"description\": \"इस वर्कशीट में विभाजन के 10 सवाल हैं। यह उन बच्चों के लिए है जो अभी विभाजन सीख रहे हैं। हर सवाल के सही या गलत होने की जानकारी दी जाएगी।\",\n" +
                "              \"domain\": [\n" +
                "                \"numeracy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/do_30013040_1467630869906.ecar\",\n" +
                "              \"es_metadata_id\": \"do_30013040\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_30013040\",\n" +
                "              \"imageCredits\": [\n" +
                "                \"ekstep\",\n" +
                "                \"Parabal Singh\",\n" +
                "                \"EkStep\"\n" +
                "              ],\n" +
                "              \"keywords\": [\n" +
                "                \"vibhajan\",\n" +
                "                \"ek ank ka vibhajan\",\n" +
                "                \"ek ank bhag\",\n" +
                "                \"bhag\",\n" +
                "                \"divide\",\n" +
                "                \"division\"\n" +
                "              ],\n" +
                "              \"language\": [\n" +
                "                \"Hindi\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-07-04T11:14:30.202+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-07-04T10:04:56.258+0000\",\n" +
                "              \"lastUpdatedBy\": \"237\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:03:44.302+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_averageInteractionsPerMin\": 33.57,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 3.29,\n" +
                "              \"me_averageTimespentPerSession\": 29.76,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 7,\n" +
                "              \"me_totalDownloads\": 7,\n" +
                "              \"me_totalInteractions\": 383,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 23,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 684.58,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"एक-अंक का विभाजन (Division)\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"optStatus\": \"Complete\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Parabal Partap Singh\",\n" +
                "              \"pkgVersion\": 2,\n" +
                "              \"popularity\": 666.35,\n" +
                "              \"portalOwner\": \"334\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/do_30013040_1467630869906.ecar\",\n" +
                "              \"size\": 1.492166e+06,\n" +
                "              \"soundCredits\": [\n" +
                "                \"ekstep\"\n" +
                "              ],\n" +
                "              \"source\": \"EkStep\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"tags\": [\n" +
                "                \"ek ank ka vibhajan\",\n" +
                "                \"vibhajan\",\n" +
                "                \"division\",\n" +
                "                \"divide\",\n" +
                "                \"bhag\",\n" +
                "                \"ek ank bhag\"\n" +
                "              ],\n" +
                "              \"template\": \"\",\n" +
                "              \"versionKey\": \"1497243997987\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"c43dc349-f57a-4705-a289-3fa470149500\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Stories\",\n" +
                "              \"hn\": \"लोकप्रिय कहानियां\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Story\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:20:24.849+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/adjective_347_1468924253_1468924296671.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/part3_347_1474371142_1474371299864.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.1295\",\n" +
                "              \"collaborators\": [\n" +
                "                \"400\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Story\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"347\",\n" +
                "              \"createdOn\": \"2016-07-19T10:30:39.961+0000\",\n" +
                "              \"creator\": \"Debasis Singh\",\n" +
                "              \"description\": \"test_eslpart3\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_eslpart3_1474371437276_do_20043457.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20043457\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20043457\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-09-20T11:37:19.038+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-09-20T11:35:53.355+0000\",\n" +
                "              \"lastUpdatedBy\": \"331\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:05:29.917+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 4,\n" +
                "              \"me_averageInteractionsPerMin\": 33.1,\n" +
                "              \"me_averageRating\": 3,\n" +
                "              \"me_averageSessionsPerDevice\": 6.6,\n" +
                "              \"me_averageTimespentPerSession\": 115.32,\n" +
                "              \"me_imagesCount\": 65,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 10,\n" +
                "              \"me_totalDownloads\": 34,\n" +
                "              \"me_totalInteractions\": 4199,\n" +
                "              \"me_totalRatings\": 3,\n" +
                "              \"me_totalSessionsCount\": 66,\n" +
                "              \"me_totalSideloads\": 0,\n" +
                "              \"me_totalTimespent\": 7611.39,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"test_eslpart3\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 41576,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debasis Singh\",\n" +
                "              \"pkgVersion\": 17,\n" +
                "              \"popularity\": 7611.39,\n" +
                "              \"portalOwner\": \"347\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/adjective_347_1468924253_1468924296671.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/test_eslpart3_1474371437276_do_20043457.ecar\",\n" +
                "              \"size\": 1.2355598e+07,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"tempData\": \"08\",\n" +
                "              \"template\": \"do_20043409\",\n" +
                "              \"versionKey\": \"1497244824849\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-12T05:20:24.319+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/icon-sound_347_1468562860_1468562895967.thumb.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"artifactUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/content/bearandelephant-english_347_1473831900_1473832046025.zip\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.literacy.story.1207\",\n" +
                "              \"collaborators\": [\n" +
                "                \"400\"\n" +
                "              ],\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"concepts\": [\n" +
                "                \"LO52\"\n" +
                "              ],\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Story\",\n" +
                "              \"copyright\": \"\",\n" +
                "              \"createdBy\": \"347\",\n" +
                "              \"createdOn\": \"2016-07-15T06:06:38.762+0000\",\n" +
                "              \"creator\": \"Debasis Singh\",\n" +
                "              \"description\": \"test_hathibhaluenglish\",\n" +
                "              \"domain\": [\n" +
                "                \"literacy\"\n" +
                "              ],\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/test_hathibhaluenglish_1473832150511_do_20043351.ecar\",\n" +
                "              \"es_metadata_id\": \"do_20043351\",\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Kindergarten\",\n" +
                "                \"Grade 1\",\n" +
                "                \"Grade 2\",\n" +
                "                \"Grade 3\",\n" +
                "                \"Grade 4\",\n" +
                "                \"Grade 5\",\n" +
                "                \"Other\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"do_20043351\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishedOn\": \"2016-09-14T05:49:13.014+0000\",\n" +
                "              \"lastSubmittedOn\": \"2016-09-14T05:48:02.603+0000\",\n" +
                "              \"lastUpdatedBy\": \"200\",\n" +
                "              \"lastUpdatedOn\": \"2017-05-15T07:05:26.686+0000\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_audiosCount\": 8,\n" +
                "              \"me_averageInteractionsPerMin\": 25.19,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 9.8,\n" +
                "              \"me_averageTimespentPerSession\": 119.67,\n" +
                "              \"me_imagesCount\": 80,\n" +
                "              \"me_timespentDraft\": 0,\n" +
                "              \"me_timespentReview\": 0,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 5,\n" +
                "              \"me_totalDownloads\": 19,\n" +
                "              \"me_totalInteractions\": 2462,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 49,\n" +
                "              \"me_totalSideloads\": 2,\n" +
                "              \"me_totalTimespent\": 5863.84,\n" +
                "              \"me_videosCount\": 0,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.ekstep.ecml-archive\",\n" +
                "              \"name\": \"test_hathibhaluenglish\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 41470,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.quiz.app\",\n" +
                "              \"owner\": \"Debasis Singh\",\n" +
                "              \"pkgVersion\": 26,\n" +
                "              \"popularity\": 5863.84,\n" +
                "              \"portalOwner\": \"347\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/language_assets/icon-sound_347_1468562860_1468562895967.png\",\n" +
                "              \"publisher\": \"\",\n" +
                "              \"s3Key\": \"ecar_files/test_hathibhaluenglish_1473832150511_do_20043351.ecar\",\n" +
                "              \"size\": 1.8728695e+07,\n" +
                "              \"source\": \"\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"template\": \"domain_12843\",\n" +
                "              \"versionKey\": \"1497244824319\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"346ee3cd-d91b-4ed4-9443-c3f84b2fa1e0\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"display\": {\n" +
                "            \"name\": {\n" +
                "              \"en\": \"Activities\",\n" +
                "              \"hn\": \"लोकप्रिय कहानियां\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"search\": {\n" +
                "            \"query\": \"\",\n" +
                "            \"limit\": 10,\n" +
                "            \"sort_by\": {\n" +
                "              \"popularity\": \"desc\"\n" +
                "            },\n" +
                "            \"filters\": {\n" +
                "              \"compatibilityLevel\": {\n" +
                "                \"max\": 3,\n" +
                "                \"min\": 1\n" +
                "              },\n" +
                "              \"contentType\": [\n" +
                "                \"Game\"\n" +
                "              ],\n" +
                "              \"objectType\": [\n" +
                "                \"Content\"\n" +
                "              ],\n" +
                "              \"status\": [\n" +
                "                \"Live\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"facets\": [\n" +
                "              \"contentType\",\n" +
                "              \"domain\",\n" +
                "              \"ageGroup\",\n" +
                "              \"language\",\n" +
                "              \"gradeLevel\"\n" +
                "            ],\n" +
                "            \"mode\": \"soft\"\n" +
                "          },\n" +
                "          \"recommend\": null,\n" +
                "          \"contents\": [\n" +
                "            {\n" +
                "              \"SYS_INTERNAL_LAST_UPDATED_ON\": \"2017-06-08T14:44:16.193+0000\",\n" +
                "              \"ageGroup\": [\n" +
                "                \"5-6\"\n" +
                "              ],\n" +
                "              \"appIcon\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/games/1448441481821_FinalIcon.png\",\n" +
                "              \"appId\": \"qa.ekstep.in\",\n" +
                "              \"audience\": [\n" +
                "                \"Learner\"\n" +
                "              ],\n" +
                "              \"authoringScore\": 10,\n" +
                "              \"channel\": \"in.ekstep\",\n" +
                "              \"code\": \"org.ekstep.delta\",\n" +
                "              \"collections\": [\n" +
                "                \n" +
                "              ],\n" +
                "              \"communication_scheme\": \"FILE\",\n" +
                "              \"compatibilityLevel\": 1,\n" +
                "              \"consumerId\": \"2c43f136-c02f-4494-9fb9-fd228e2c77e6\",\n" +
                "              \"contentDisposition\": \"inline\",\n" +
                "              \"contentEncoding\": \"gzip\",\n" +
                "              \"contentType\": \"Game\",\n" +
                "              \"createdBy\": \"EkStep\",\n" +
                "              \"createdOn\": \"2016-08-18T07:42:46.034+0000\",\n" +
                "              \"creator\": \"Ekstep\",\n" +
                "              \"description\": \"Place Value Addition Subtraction Game\",\n" +
                "              \"developer\": \"Filament Games\",\n" +
                "              \"downloadUrl\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/ecar_files/org_ekstep_delta_16092016.ecar\",\n" +
                "              \"es_metadata_id\": \"org.ekstep.delta\",\n" +
                "              \"genieScore\": 10,\n" +
                "              \"gradeLevel\": [\n" +
                "                \"Grade 1\"\n" +
                "              ],\n" +
                "              \"graph_id\": \"domain\",\n" +
                "              \"idealScreenDensity\": \"hdpi\",\n" +
                "              \"idealScreenSize\": \"normal\",\n" +
                "              \"identifier\": \"org.ekstep.delta\",\n" +
                "              \"language\": [\n" +
                "                \"English\"\n" +
                "              ],\n" +
                "              \"lastPublishDate\": \"2016-05-13T13:36:10.281+0000\",\n" +
                "              \"lastPublishedOn\": \"2016-05-11T08:04:39.404+0000\",\n" +
                "              \"lastUpdatedOn\": \"2017-06-04T16:17:51.469+0000\",\n" +
                "              \"launchUrl\": \"org.ekstep.delta\",\n" +
                "              \"license\": \"Creative Commons Attribution (CC BY)\",\n" +
                "              \"me_averageInteractionsPerMin\": 18.62,\n" +
                "              \"me_averageRating\": 0,\n" +
                "              \"me_averageSessionsPerDevice\": 5.02,\n" +
                "              \"me_averageTimespentPerSession\": 351.13,\n" +
                "              \"me_totalComments\": 0,\n" +
                "              \"me_totalDevices\": 338,\n" +
                "              \"me_totalDownloads\": 37,\n" +
                "              \"me_totalInteractions\": 184793,\n" +
                "              \"me_totalRatings\": 0,\n" +
                "              \"me_totalSessionsCount\": 1696,\n" +
                "              \"me_totalSideloads\": 5,\n" +
                "              \"me_totalTimespent\": 595514,\n" +
                "              \"mediaType\": \"content\",\n" +
                "              \"mimeType\": \"application/vnd.android.package-archive\",\n" +
                "              \"name\": \"Take Off\",\n" +
                "              \"nodeType\": \"DATA_NODE\",\n" +
                "              \"node_id\": 0,\n" +
                "              \"objectType\": \"Content\",\n" +
                "              \"os\": [\n" +
                "                \"All\"\n" +
                "              ],\n" +
                "              \"osId\": \"org.ekstep.delta\",\n" +
                "              \"owner\": \"EkStep\",\n" +
                "              \"pkgVersion\": 5,\n" +
                "              \"popularity\": 587897,\n" +
                "              \"portalOwner\": \"EkStep\",\n" +
                "              \"posterImage\": \"https://ekstep-public-qa.s3-ap-south-1.amazonaws.com/games/1452332896911_promo_180x120.jpg\",\n" +
                "              \"publisher\": \"EkStep\",\n" +
                "              \"s3Key\": \"ecar_files/org.ekstep.takeoff_1463146569898.ecar\",\n" +
                "              \"size\": 3.4851095e+07,\n" +
                "              \"source\": \"EkStep\",\n" +
                "              \"status\": \"Live\",\n" +
                "              \"subject\": \"numeracy\",\n" +
                "              \"tempData\": \"15\",\n" +
                "              \"versionKey\": \"1496933056193\",\n" +
                "              \"visibility\": \"Default\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"resmsgid\": \"cd0d7d7a-c0d3-4c07-a695-944e543a029b\",\n" +
                "          \"apiid\": \"ekstep.composite-search.search\",\n" +
                "          \"filterModifiable\": true\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
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

    public static void clearTagTableDBEntry() {
        try {
            int count = getDatabase().delete("telemetry_tags", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "Unable to delete tags DB entry");
        }
    }

    public static void clearNotificationTableDBEntry() {
        try {
            int count = getDatabase().delete("notifications", "1", null);
            Log.v("Count:::::", "" + count);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.v(TAG, "Unable to delete notifications DB entry");
        }
    }
}
