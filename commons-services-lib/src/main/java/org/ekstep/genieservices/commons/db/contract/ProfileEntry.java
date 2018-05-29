package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    public static final String COLUMN_NAME_PROFILE_IMAGE = "profile_image";
    public static final String COLUMN_NAME_PROFILE_TYPE = "profile_type";
    public static final String COLUMN_NAME_SUBJECT = "subject";
    public static final String COLUMN_NAME_GRADE = "grade";
    public static final String COLUMN_NAME_SYLLABUS = "syllabus";

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

    public static String getAlterEntryForProfileImage() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_PROFILE_IMAGE + DbConstants.TEXT_TYPE;
    }

    public static List<String> getAlterEntryForProfileSubjectnType() {
        return Arrays.asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_SUBJECT + DbConstants.TEXT_TYPE + "  DEFAULT '';",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_PROFILE_TYPE + DbConstants.TEXT_TYPE + "  DEFAULT 'teacher';",
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_GRADE + DbConstants.TEXT_TYPE + "  DEFAULT '';");
    }

    public static String getAlterEntryForProfileSyllabus() {
        return "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_SYLLABUS + DbConstants.TEXT_TYPE + "  DEFAULT '';";
    }

    public static List<String> getUpdateProfileImage(String basePath) {
        return Arrays.asList(
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_1.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar1"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_2.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar2"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_3.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar3"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_4.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar4"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_5.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar5"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_6.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar6"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_normal_7.png", COLUMN_NAME_AVATAR, "@drawable/ic_avatar7"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge01.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge1"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge02.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge2"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge03.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge3"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge04.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge4"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge05.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge5"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge06.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge6"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge07.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge7"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge08.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge8"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge09.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge9"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/img_badge10.png", COLUMN_NAME_AVATAR, "@drawable/ic_badge10"),
                String.format(Locale.US, "UPDATE %s SET %s = '%s' where %s = '%s';", TABLE_NAME, COLUMN_NAME_PROFILE_IMAGE, basePath + "/avatar_anonymous.png", COLUMN_NAME_AVATAR, "")
        );
    }


}
