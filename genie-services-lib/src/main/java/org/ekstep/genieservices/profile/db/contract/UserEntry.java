package org.ekstep.genieservices.profile.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class UserEntry implements BaseColumns {

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_UID = "uid";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
