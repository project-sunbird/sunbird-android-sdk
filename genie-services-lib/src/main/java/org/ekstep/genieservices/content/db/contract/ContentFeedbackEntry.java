package org.ekstep.genieservices.content.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public class ContentFeedbackEntry implements BaseColumns {

    public static final String TABLE_NAME = "feedback";
    public static final String COLUMN_NAME_CONTENT_ID = "identifier";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_RATING = "rating";
    public static final String COLUMN_NAME_COMMENTS = "comments";
    public static final String COLUMN_NAME_CREATED_AT = "created_at";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_CONTENT_ID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_RATING + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_COMMENTS + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_CREATED_AT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                " UNIQUE (" + COLUMN_NAME_UID + DbConstants.COMMA_SEP + COLUMN_NAME_CONTENT_ID + ") ON CONFLICT REPLACE" +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
