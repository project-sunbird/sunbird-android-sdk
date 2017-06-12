package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 18/03/16.
 *
 * @author anil
 */
public abstract class LearnerContentSummaryEntry implements BaseColumns {

    public static final String TABLE_NAME = "learner_content_summary";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_CONTENT_ID = "content_id";
    public static final String COLUMN_NAME_AVG_TS = "avg_ts";
    public static final String COLUMN_NAME_SESSIONS = "sessions";
    public static final String COLUMN_NAME_TOTAL_TS = "total_ts";
    public static final String COLUMN_NAME_LAST_UPDATED_ON = "last_updated_on";
    public static final String COLUMN_NAME_HIERARCHY_DATA = "h_data";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_CONTENT_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_AVG_TS + DbConstants.REAL_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SESSIONS + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_TOTAL_TS + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LAST_UPDATED_ON + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_HIERARCHY_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                " UNIQUE (" + COLUMN_NAME_UID + DbConstants.COMMA_SEP + COLUMN_NAME_CONTENT_ID + DbConstants.COMMA_SEP + COLUMN_NAME_HIERARCHY_DATA + ") ON CONFLICT REPLACE" +
                " )";
    }


    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
