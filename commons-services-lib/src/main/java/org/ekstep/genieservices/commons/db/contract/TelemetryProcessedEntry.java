package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class TelemetryProcessedEntry implements BaseColumns {
    public static final String TABLE_NAME = "processed_telemetry";
    public static final String COLUMN_NAME_MSG_ID = "msg_id";
    public static final String COLUMN_NAME_DATA = "data";
    public static final String COLUMN_NAME_NUMBER_OF_EVENTS = "event_count";
    public static final String COLUMN_NAME_PRIORITY = "priority";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TelemetryProcessedEntry.TABLE_NAME + " (" +
                TelemetryProcessedEntry._ID + " INTEGER PRIMARY KEY," +
                TelemetryProcessedEntry.COLUMN_NAME_MSG_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                TelemetryProcessedEntry.COLUMN_NAME_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                TelemetryProcessedEntry.COLUMN_NAME_NUMBER_OF_EVENTS + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                TelemetryProcessedEntry.COLUMN_NAME_PRIORITY + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TelemetryProcessedEntry.TABLE_NAME;
    }
}
