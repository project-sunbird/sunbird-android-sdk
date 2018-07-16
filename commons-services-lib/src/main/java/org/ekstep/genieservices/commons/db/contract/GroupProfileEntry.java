package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 07/16/2018.
 *
 * @author anil
 */
public abstract class GroupProfileEntry implements BaseColumns {

    public static final String TABLE_NAME = "group_profile";
    public static final String COLUMN_NAME_GID = "gid";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_EPOCH_TIMESTAMP = "epoch_timestamp";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_EPOCH_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                " UNIQUE (" + COLUMN_NAME_GID + DbConstants.COMMA_SEP + COLUMN_NAME_UID + ") ON CONFLICT REPLACE" +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
