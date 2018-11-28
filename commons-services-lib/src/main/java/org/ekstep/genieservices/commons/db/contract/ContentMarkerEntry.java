package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 11/22/2018.
 *
 * @author anil
 */
public abstract class ContentMarkerEntry implements BaseColumns {

    public static final String TABLE_NAME = "content_marker";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_CONTENT_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_EPOCH_TIMESTAMP = "epoch_timestamp";
    public static final String COLUMN_NAME_DATA = "data";
    public static final String COLUMN_NAME_EXTRA_INFO = "extra_info";
    public static final String COLUMN_NAME_MARKER = "marker";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_CONTENT_IDENTIFIER + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_EPOCH_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_EXTRA_INFO + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MARKER + DbConstants.INT_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
