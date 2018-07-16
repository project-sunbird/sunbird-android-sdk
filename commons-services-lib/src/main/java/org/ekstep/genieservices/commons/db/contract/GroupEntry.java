package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 16/7/18.
 * shriharsh
 */
public class GroupEntry implements BaseColumns {

    public static final String TABLE_NAME = "group";
    public static final String COLUMN_NAME_GID = "gid";
    public static final String COLUMN_NAME_GROUP_NAME = "name";
    public static final String COLUMN_NAME_SYLLABUS = "syllabus";
    public static final String COLUMN_NAME_GRADE = "grade";
    public static final String COLUMN_NAME_CREATED_AT = "created_at";
    public static final String COLUMN_NAME_UPDATED_AT = "updated_at";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_GROUP_NAME + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SYLLABUS + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_GRADE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_CREATED_AT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_UPDATED_AT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                " )";
    }

}
