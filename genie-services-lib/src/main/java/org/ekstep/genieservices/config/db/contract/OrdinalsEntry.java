package org.ekstep.genieservices.config.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class OrdinalsEntry implements BaseColumns {

    public static final String TABLE_NAME = "ordinals";
    public static final String COLUMN_NAME_ORDINAL_IDENTIFIER = "ordinal_identifier";
    public static final String COLUMN_NAME_ORDINAL_JSON = "ordinal_json";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ORDINAL_IDENTIFIER + DbConstants.TEXT_TYPE + ", " +
                COLUMN_NAME_ORDINAL_JSON + DbConstants.BLOB_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
