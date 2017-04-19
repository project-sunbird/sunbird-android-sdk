package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

import java.util.Locale;

public class LanguageEntry implements BaseColumns {

    public static final String TABLE_NAME = "languages";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_CODE = "code";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NAME + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_CODE + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getBootstrapEntry() {
        String insertQuery = String.format(Locale.US, "INSERT INTO %s(%s,%s) values", TABLE_NAME, COLUMN_NAME_NAME, COLUMN_NAME_CODE);
        insertQuery += String.format(Locale.US, "('%s','%s'), ('%s','%s'), ('%s','%s');", "English", "en", "ಕನ್ನಡ", "kn", "हिंदी", "hi");
        return insertQuery;
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
