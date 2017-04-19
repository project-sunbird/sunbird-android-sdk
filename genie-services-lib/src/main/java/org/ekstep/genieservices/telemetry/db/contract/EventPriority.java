package org.ekstep.genieservices.telemetry.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class EventPriority implements BaseColumns {

    public static final String TABLE_NAME = "event_priority";
    public static final String COLUMN_NAME_EVENT = "event";
    public static final String COLUMN_PRIORITY = "priority";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + EventPriority.TABLE_NAME + " (" +
                EventPriority._ID + " INTEGER PRIMARY KEY," +
                EventPriority.COLUMN_NAME_EVENT + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                EventPriority.COLUMN_PRIORITY + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + EventPriority.TABLE_NAME;
    }
}
