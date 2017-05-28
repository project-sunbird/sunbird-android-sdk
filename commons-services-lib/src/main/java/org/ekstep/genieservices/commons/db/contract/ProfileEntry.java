package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Arrays;
import java.util.List;

public abstract class ProfileEntry implements BaseColumns {

    public static final String TABLE_NAME = "profiles";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_HANDLE = "handle";
    public static final String COLUMN_NAME_AVATAR = "avatar";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_GENDER = "gender";
    public static final String COLUMN_NAME_STANDARD = "standard";
    public static final String COLUMN_NAME_LANGUAGE = "language";
    public static final String COLUMN_NAME_DAY = "day";
    public static final String COLUMN_NAME_MONTH = "month";
    public static final String COLUMN_NAME_CREATED_AT = "created_at";
    public static final String COLUMN_NAME_IS_GROUP_USER = "is_group_user";
    public static final String COLUMN_NAME_MEDIUM = "medium";
    public static final String COLUMN_NAME_BOARD = "board";

    public static final String getCreateEntry() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_HANDLE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_AVATAR + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_AGE + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_GENDER + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_STANDARD + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_LANGUAGE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_DAY + DbConstants.INT_TYPE + " NOT NULL DEFAULT -1" + DbConstants.COMMA_SEP +
                COLUMN_NAME_MONTH + DbConstants.INT_TYPE + " NOT NULL DEFAULT -1" + DbConstants.COMMA_SEP +
                COLUMN_NAME_IS_GROUP_USER + DbConstants.INT_TYPE + " NOT NULL DEFAULT 0" +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static List<String> getAlterEntryDayAndMonthToProfile() {
        return Arrays.asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_DAY + DbConstants.INT_TYPE + " NOT NULL DEFAULT -1;",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_MONTH + DbConstants.INT_TYPE + " NOT NULL DEFAULT -1;");
    }

    public static String getAlterEntryIsGroupUser() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_IS_GROUP_USER + DbConstants.INT_TYPE + " NOT NULL DEFAULT 0;";
    }

    public static String getAlterEntryForCreateAt() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_CREATED_AT + DbConstants.INT_TYPE;
    }

    public static List<String> getAlterEntryForMediumnBoard() {
        return Arrays.asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_MEDIUM + DbConstants.TEXT_TYPE + "  DEFAULT '';",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_BOARD + DbConstants.TEXT_TYPE + "  DEFAULT '';");
    }

    public static String getUpdateCreatedAtEntry() {
        return "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME_CREATED_AT + " = " + DateUtil.getEpochTime() + ";";
    }

}
