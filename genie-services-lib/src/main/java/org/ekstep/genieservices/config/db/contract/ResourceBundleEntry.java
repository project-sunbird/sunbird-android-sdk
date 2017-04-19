package org.ekstep.genieservices.config.db.contract;



import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;


public class ResourceBundleEntry implements BaseColumns {
    public static final String TABLE_NAME = "resourceBundle";
    public static final String COLUMN_NAME_BUNDLE_IDENTIFIER = "bundle_identifier";
    public static final String COLUMN_NAME_BUNDLE_JSON = "bundle_json";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + ResourceBundleEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_BUNDLE_IDENTIFIER + DbConstants.TEXT_TYPE + ", " +
                COLUMN_NAME_BUNDLE_JSON + DbConstants.BLOB_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
