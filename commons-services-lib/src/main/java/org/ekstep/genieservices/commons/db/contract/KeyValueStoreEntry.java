package org.ekstep.genieservices.commons.db.contract;


import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class KeyValueStoreEntry implements BaseColumns {
    public static final String TABLE_NAME = "key_value";
    public static final String COLUMN_NAME_KEY = "key";
    public static final String COLUMN_NAME_VALUE = "value";


    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_KEY + DbConstants.TEXT_TYPE + ", " +
                COLUMN_NAME_VALUE + DbConstants.TEXT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
