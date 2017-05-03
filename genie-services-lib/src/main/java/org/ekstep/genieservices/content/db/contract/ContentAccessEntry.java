package org.ekstep.genieservices.content.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 1/24/2017.
 *
 * @author anil
 */
public abstract class ContentAccessEntry implements BaseColumns {

    public static final String TABLE_NAME = "content_access";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_EPOCH_TIMESTAMP = "epoch_timestamp";
    public static final String COLUMN_NAME_STATUS = "status";     // imported or new = 0, viewed = 1, partiallyPlayed = 2, fullyPlayed = 3
    public static final String COLUMN_NAME_CONTENT_TYPE = "content_type";
    public static final String COLUMN_NAME_LEARNER_STATE = "learner_state";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_IDENTIFIER + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_EPOCH_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_STATUS + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_CONTENT_TYPE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LEARNER_STATE + DbConstants.BLOB_TYPE +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
