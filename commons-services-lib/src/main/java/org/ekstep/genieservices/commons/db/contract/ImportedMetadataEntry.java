package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class ImportedMetadataEntry implements BaseColumns {

    public static final String TABLE_NAME = "imported_metadata";
    public static final String COLUMN_NAME_IMPORTED_ID = "imported_id";
    public static final String COLUMN_NAME_DEVICE_ID = "device_id";
    public static final String COLUMN_NAME_COUNT = "count";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_IMPORTED_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_DEVICE_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_COUNT + DbConstants.INT_TYPE +
                " )";
    }

    public static String getUpdateEntryForCountColumn() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_COUNT + " INTEGER NULL;";
    }


    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
