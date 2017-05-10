package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class TelemetryEntry implements BaseColumns {

    public static final String TABLE_NAME = "telemetry";
    public static final String COLUMN_NAME_EVENT_TYPE = "event_type";
    public static final String COLUMN_NAME_EVENT = "event";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_PRIORITY = "priority";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TelemetryEntry.TABLE_NAME + " (" +
                TelemetryEntry._ID + " INTEGER PRIMARY KEY," +
                TelemetryEntry.COLUMN_NAME_EVENT_TYPE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                TelemetryEntry.COLUMN_NAME_EVENT + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                TelemetryEntry.COLUMN_NAME_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                TelemetryEntry.COLUMN_NAME_PRIORITY + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TelemetryEntry.TABLE_NAME;
    }
}
