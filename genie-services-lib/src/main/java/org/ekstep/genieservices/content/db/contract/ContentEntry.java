package org.ekstep.genieservices.content.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public abstract class ContentEntry implements BaseColumns {
    public static final String TABLE_NAME = "content";
    public static final String COLUMN_NAME_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_SERVER_DATA = "server_data";
    public static final String COLUMN_NAME_LOCAL_DATA = "local_data";
    public static final String COLUMN_NAME_MIME_TYPE = "mime_type";
    public static final String COLUMN_NAME_PATH = "path";
    public static final String COLUMN_NAME_INDEX = "search_index";
    public static final String COLUMN_NAME_VISIBILITY = "visibility";   // Visibility could be Default or Parent
    public static final String COLUMN_NAME_SERVER_LAST_UPDATED_ON = "server_last_updated_on";
    public static final String COLUMN_NAME_LOCAL_LAST_UPDATED_ON = "local_last_updated_on";
    public static final String COLUMN_NAME_MANIFEST_VERSION = "manifest_version";
    public static final String COLUMN_NAME_REF_COUNT = "ref_count";
    public static final String COLUMN_NAME_CONTENT_STATE = "content_state"; // 0 - Seen but not available (only serverData will be available), 1 - Only spine, 2 - Artifact available
    public static final String COLUMN_NAME_CONTENT_TYPE = "content_type";   // Content type could be story, worksheet, game, collection, textbook.

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_IDENTIFIER + DbConstants.TEXT_TYPE + " UNIQUE NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_SERVER_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LOCAL_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MIME_TYPE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_PATH + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_INDEX + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_VISIBILITY + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SERVER_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LOCAL_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MANIFEST_VERSION + DbConstants.TEXT_TYPE +
                " )";
    }

    public static List<String> getUpdateEntryWithSearchIndexColumn() {
        return asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_INDEX + DbConstants.TEXT_TYPE + DbConstants.NULL + ";"
                , "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_VISIBILITY + DbConstants.TEXT_TYPE + DbConstants.NULL + ";"
                , "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_SERVER_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.NULL + ";"
                , "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_LOCAL_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.NULL + ";"
                , "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_MANIFEST_VERSION + DbConstants.TEXT_TYPE + DbConstants.NULL + ";"
        );
    }

    public static String getAlterEntryForRefCount() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_REF_COUNT + DbConstants.INT_TYPE + " NOT NULL DEFAULT 1;";
    }

    public static String getAlterEntryForContentState() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_CONTENT_STATE + DbConstants.INT_TYPE + " NOT NULL DEFAULT 2;";
    }

    public static String getAlterEntryForContentType() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_CONTENT_TYPE + DbConstants.TEXT_TYPE + ";";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static String aserData() {
        return "{\"name\":\"Aser\",\"identifier\":\"org.ekstep.aser\",\"description\":\"Aser\",\"appIcon\":\"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/aser.png\",\"launchUrl\":\"org.ekstep.aser\",\"downloadUrl\":null,\"activity_class\":\".activities.SplashActivity\",\"code\":\"org.ekstep.aser\",\"grayScaleAppIcon\":\"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/aser-gs.png\",\"osId\":\"org.ekstep.aser\",\"communication_scheme\":\"INTENT\"}";
    }

    private static String aserLiteData() {
        return "{" +
                "\"appIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1445343655325_ASER_logo.PNG\"," +
                "\"communication_scheme\": \"INTENT\"," +
                "\"subject\": \"numeracy\"," +
                "\"posterImage\": \"https://drive.google.com/drive/u/0/folders/0B0tmbM4EgUdIWU82UU14UVduVW8\"," +
                "\"launchUrl\": \"org.ekstep.aser.lite\"," +
                "\"downloadUrl\": null," +
                "\"activity_class\": \".activities.SplashActivity\"," +
                "\"code\": \"org.ekstep.aser.lite\"," +
                "\"osId\": \"org.ekstep.aser.lite\"," +
                "\"description\": null," +
                "\"name\": \"Lakhon mein ek\"," +
                "\"identifier\": \"numeracy_363\"," +
                "\"filter\": null," +
                "\"grayScaleAppIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1445343682587_aser-gs.png\"," +
                "\"pkgVersion\": null" +
                "}";
    }

    private static String quizApp() {
        return "{" +
                "\"appIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1448441693566_akshara_worksheet.png\"," +
                "\"communication_scheme\": \"INTENT\"," +
                "\"subject\": \"numeracy\"," +
                "\"posterImage\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1448441693566_akshara_worksheet.png\"," +
                "\"launchUrl\": \"org.ekstep.quiz.app\"," +
                "\"downloadUrl\": \"https://ekstep-public.s3-ap-southeast-1.amazonaws.com/content/akshara_ws_nov25_1448451877172.zip\"," +
                "\"activity_class\": \".MainActivity\"," +
                "\"code\": \"org.akshara.worksheet1\"," +
                "\"osId\": \"org.ekstep.quiz.app\"," +
                "\"description\": \"Worksheet for children of Grade 4 and Grade 5\"," +
                "\"name\": \"GKA Akshara Worksheet\"," +
                "\"identifier\": \"numeracy_369\"," +
                "\"filter\": null," +
                "\"grayScaleAppIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1444390667474_img_logo1.png\"," +
                "\"pkgVersion\": 1" +
                "}";
    }

    private static String aksharaApp() {
        return "{" +
                "\"appIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1445251040215_img_logo.png\"," +
                "\"communication_scheme\": \"INTENT\"," +
                "\"subject\": \"numeracy\"," +
                "\"posterImage\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1445251040215_img_logo.png\"," +
                "\"launchUrl\": null," +
                "\"downloadUrl\": null," +
                "\"activity_class\": \".MainActivity\"," +
                "\"code\": \"org.akshara\"," +
                "\"osId\": \"org.akshara\"," +
                "\"description\": \"Akshara Child Database Selection\"," +
                "\"name\": \"AKSHARA PARTNER APP\"," +
                "\"identifier\": \"numeracy_370\"," +
                "\"filter\": null," +
                "\"grayScaleAppIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/1445251051097_img_logo1.png\"," +
                "\"pkgVersion\": null" +
                "}";
    }

    private static String basicLitScreener() {
        return "{" +
                "\"appIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/litscrn_icon.png\"," +
                "\"communication_scheme\": \"FILE\"," +
                "\"subject\": \"literacy_v2\"," +
                "\"posterImage\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/litscrn_icon.png\"," +
                "\"launchUrl\": \"org.ekstep.lit.scrnr.kan.basic\"," +
                "\"downloadUrl\": null," +
                "\"activity_class\": null," +
                "\"code\": \"org.ekstep.lit.scrnr.kan.basic\"," +
                "\"osId\": \"org.ekstep.lit.scrnr.kan.basic\"," +
                "\"description\": \"EkStep Literacy Screener is an assessment tool that measures a primary school child awareness and competency across multiple components of literacy viz.vocabulary, Akshara of Indian languages, decoding, reading and listening comprehension using a scientifically validated set of content items. The Screener attempts to place a child at a learning level and maps the lag/advance each child has against these components.\"," +
                "\"name\": \"EkStep Literacy Screener\"," +
                "\"identifier\": \"org.ekstep.lit.scrnr.kan.basic\"," +
                "\"filter\": null," +
                "\"grayScaleAppIcon\": \"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/litscrn_icon-gs.png\"," +
                "\"pkgVersion\": null" +
                "}";
    }


    private static String aksharaWorksheet() {
        return "{" +
                "\"appIcon\": \"1448441693566_akshara_worksheet.png\"," +
                "\"downloadUrl\": \"1449227500674_akshara_quiz.zip\"," +
                "\"grayScaleAppIcon\": \"1444390667474_img_logo1.png\"," +
                "\"posterImage\": \"\"," +
                "\"communication_scheme\": \"INTENT\"," +
                "\"subject\": \"numeracy\"," +
                "\"launchUrl\": \"org.ekstep.quiz.app\"," +
                "\"activity_class\": \".MainActivity\"," +
                "\"code\": \"org.akshara.worksheet1\"," +
                "\"osId\": \"org.ekstep.quiz.app\"," +
                "\"description\": \"Worksheet for children of Grade 4 and Grade 5\"," +
                "\"name\": \"GKA Akshara Worksheet\"," +
                "\"identifier\": \"numeracy_377\"," +
                "\"filter\": null," +
                "\"pkgVersion\": 11," +
                "\"mimeType\": \"application/vnd.ekstep.ecml-archive\"," +
                "\"minSupportedVersion\": \"8\"" +
                "}";
    }

    private static HashMap<String, String> getData() {
        HashMap<String, String> seedData = new HashMap<>();
        seedData.put("org.ekstep.aser", aserData());
        seedData.put("numeracy_363", aserLiteData());
        seedData.put("numeracy_369", quizApp());
        seedData.put("numeracy_370", aksharaApp());
        seedData.put("org.ekstep.lit.scrnr.kan.basic", basicLitScreener());
        seedData.put("numeracy_377", aksharaWorksheet());
        return seedData;
//        return Arrays.asList(aserData(),aserLiteData(),quizApp(),aksharaApp(),basicLitScreener());
    }

    public static final List<String> getBootstrapData() {
        ArrayList<String> queries = new ArrayList<>();
        for (String id : getData().keySet())
            queries.add(String.format(Locale.US, "insert into %s (%s,%s) values ('%s','%s')",
                    TABLE_NAME, COLUMN_NAME_IDENTIFIER, COLUMN_NAME_SERVER_DATA, id, getData().get(id)));

        return queries;
    }
}
