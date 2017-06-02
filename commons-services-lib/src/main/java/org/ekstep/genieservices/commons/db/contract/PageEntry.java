package org.ekstep.genieservices.commons.db.contract;


import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

import java.util.List;

import static java.util.Arrays.asList;

public abstract class PageEntry implements BaseColumns {
    public static final String TABLE_NAME = "pages";
    public static final String COLUMN_NAME_PAGE_IDENTIFIER = "page_identifier";
    public static final String COLUMN_NAME_PAGE_JSON = "page_json";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_STANDARD = "standard";
    public static final String COLUMN_NAME_MEDIUM = "medium";
    public static final String COLUMN_NAME_BOARD = "board";
    public static final String COLUMN_NAME_SUBJECT = "subject";
    public static final String COLUMN_EXPIRY_TIME = "expiry_time";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_PAGE_IDENTIFIER + DbConstants.TEXT_TYPE + ", " +
                COLUMN_NAME_PAGE_JSON + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static List<String> getAlterEntryForProfileAttributes() {
        return asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_AGE + DbConstants.INT_TYPE + " ;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_STANDARD + DbConstants.INT_TYPE + " ;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_MEDIUM + DbConstants.TEXT_TYPE + " ;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_BOARD + DbConstants.TEXT_TYPE + " ;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_SUBJECT + DbConstants.TEXT_TYPE + " ;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_EXPIRY_TIME + DbConstants.INT_TYPE + " ;");
    }
}
