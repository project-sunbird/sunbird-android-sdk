package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class EventPriorityEntry implements BaseColumns {

    public static final String TABLE_NAME = "event_priority";
    public static final String COLUMN_NAME_EVENT = "event";
    public static final String COLUMN_NAME_PRIORITY = "priority";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_EVENT + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_PRIORITY + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
