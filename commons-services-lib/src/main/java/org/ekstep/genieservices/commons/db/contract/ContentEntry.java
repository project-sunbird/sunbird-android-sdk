package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

public abstract class ContentEntry implements BaseColumns {

    public static final String TABLE_NAME = "content";
    public static final String COLUMN_NAME_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_SERVER_DATA = "server_data";
    public static final String COLUMN_NAME_LOCAL_DATA = "local_data";
    public static final String COLUMN_NAME_MIME_TYPE = "mime_type";
    public static final String COLUMN_NAME_PATH = "path";
    public static final String COLUMN_NAME_INDEX = "search_index";
    public static final String COLUMN_NAME_VISIBILITY = "visibility";   // Visibility could be Default or Parent
    public static final String COLUMN_NAME_SERVER_LAST_UPDATED_ON = "server_last_updated_on";
    public static final String COLUMN_NAME_LOCAL_LAST_UPDATED_ON = "local_last_updated_on";
    public static final String COLUMN_NAME_MANIFEST_VERSION = "manifest_version";
    public static final String COLUMN_NAME_REF_COUNT = "ref_count";
    public static final String COLUMN_NAME_CONTENT_STATE = "content_state"; // 0 - Seen but not available (only serverData will be available), 1 - Only spine, 2 - Artifact available
    public static final String COLUMN_NAME_CONTENT_TYPE = "content_type";   // Content type could be story, worksheet, game, collection, textbook.
    public static final String COLUMN_NAME_AUDIENCE = "audience";   // learner or instructor
    public static final String COLUMN_NAME_UID = "uid";   // list of comma separated uid

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_IDENTIFIER + DbConstants.TEXT_TYPE + " UNIQUE NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_SERVER_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LOCAL_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MIME_TYPE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_PATH + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_INDEX + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_VISIBILITY + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SERVER_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LOCAL_LAST_UPDATED_ON + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MANIFEST_VERSION + DbConstants.TEXT_TYPE +
                " )";
    }

    public static String getAlterEntryForRefCount() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_REF_COUNT + DbConstants.INT_TYPE + " NOT NULL DEFAULT 1;";
    }

    public static String getAlterEntryForContentState() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_CONTENT_STATE + DbConstants.INT_TYPE + " NOT NULL DEFAULT 2;";
    }

    public static String getAlterEntryForContentType() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_CONTENT_TYPE + DbConstants.TEXT_TYPE + ";";
    }

    public static String getAlterEntryForAudience() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_AUDIENCE + DbConstants.TEXT_TYPE + " DEFAULT 'Learner';";
    }

    public static String getAlterEntryForUid() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_UID + DbConstants.TEXT_TYPE + ";";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
