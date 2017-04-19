package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class GameEntry implements BaseColumns {

    public static final String TABLE_NAME = "game_list";
    public static final String COLUMN_NAME_DATA = "data";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                GameEntry._ID + " INTEGER PRIMARY KEY," +
                GameEntry.COLUMN_NAME_DATA + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME;
    }

    private static String aserData() {
        return "{\"name\":\"Aser\",\"identifier\":\"org.ekstep.aser\",\"description\":\"Aser\",\"appIcon\":\"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/aser.png\",\"launchUrl\":\"org.ekstep.aser\",\"downloadUrl\":null,\"activity_class\":\".activities.SplashActivity\",\"code\":\"org.ekstep.aser\",\"grayScaleAppIcon\":\"https://s3-ap-southeast-1.amazonaws.com/ekstep-public/games/aser-gs.png\",\"osId\":\"org.ekstep.aser\",\"communication_scheme\":\"INTENT\"}";
    }

    private static String AserLiteData() {
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

    private static String QuizApp() {
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

    private static String AksharaApp() {
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

    private static String BasicLitScreener() {
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

    private static List<String> getData() {
        return Arrays.asList(aserData(), AserLiteData(), QuizApp(), AksharaApp(), BasicLitScreener());
    }

    public static final List<String> getBootstrapData() {
        ArrayList<String> queries = new ArrayList<>();
        for (String data : getData())
            queries.add(String.format(Locale.US, "insert into %s (%s) values ('%s')", TABLE_NAME, COLUMN_NAME_DATA, data));

        return queries;
    }
}
