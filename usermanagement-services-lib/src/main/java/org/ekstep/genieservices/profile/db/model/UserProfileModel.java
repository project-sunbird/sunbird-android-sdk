package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.enums.ProfileType;
import org.ekstep.genieservices.commons.bean.enums.UserSource;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class UserProfileModel implements IWritable, IReadable, IUpdatable, ICleanable {

    private long id = -1;
    private Profile profile;
    private IDBSession dbSession;
    private String filterCondition;

    private UserProfileModel(IDBSession dbSession, Profile profile, String filterCondition) {
        this.profile = profile;
        this.dbSession = dbSession;
        this.filterCondition = filterCondition;
    }

    public static UserProfileModel build(IDBSession dbSession, Profile profile) {
        UserProfileModel profileModel = new UserProfileModel(dbSession, profile, "");
        return profileModel;
    }

    public static UserProfileModel find(IDBSession dbSession, String uid) {
        Profile profile = new Profile(uid);
        UserProfileModel profileModel = new UserProfileModel(dbSession, profile, "");
        dbSession.read(profileModel);
        //check if the profile was found and return null if the profile was not found
        if (StringUtil.isNullOrEmpty(profileModel.profile.getHandle())) {
            return null;
        } else {
            return profileModel;
        }
    }

    public static UserProfileModel find(IDBSession dbSession, String uid, String filterCondition) {
        Profile profile = new Profile(uid);
        UserProfileModel profileModel = new UserProfileModel(dbSession, profile, filterCondition);
        dbSession.read(profileModel);
        //check if the profile was found and return null if the profile was not found
        if (StringUtil.isNullOrEmpty(profileModel.profile.getHandle())) {
            return null;
        } else {
            return profileModel;
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public void update() {
        dbSession.update(this);
    }

    public Void save() {
        dbSession.create(this);
        return null;
    }

    public Void delete() {
        dbSession.clean(this);
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProfileEntry.COLUMN_NAME_UID, profile.getUid());
        populateContentValues(contentValues);

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            readWithoutMoving(cursor);
        }
        return this;
    }

    @Override
    public String getTableName() {
        return ProfileEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void readWithoutMoving(IResultSet cursor) {
        //The column index checks below are required because we allow exporting and importing profiles and profiles from previous Genie versions
        // may not have some of these columns.
        id = cursor.getLong(0);
        profile.setUid(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_UID)));

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_HANDLE) != -1) {
            profile.setHandle(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_HANDLE)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AVATAR) != -1) {
            profile.setAvatar(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AVATAR)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AGE) != -1) {
            profile.setAge(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_AGE)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GENDER) != -1) {
            profile.setGender(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GENDER)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_STANDARD) != -1) {
            profile.setStandard(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_STANDARD)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_LANGUAGE) != -1) {
            profile.setLanguage(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_LANGUAGE)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_DAY) != -1) {
            profile.setDay(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_DAY)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MONTH) != -1) {
            profile.setMonth(cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MONTH)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_IS_GROUP_USER) != -1) {
            boolean isGroupUser = cursor.getInt(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_IS_GROUP_USER)) == 1;
            profile.setGroupUser(isGroupUser);
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_CREATED_AT) != -1) {
            profile.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_CREATED_AT))));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MEDIUM) != -1) {
            String medium = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MEDIUM));
            if (!StringUtil.isNullOrEmpty(medium)) {
                profile.setMedium(medium.split(","));
            }

        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_BOARD) != -1) {
            String board = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_BOARD));
            if (!StringUtil.isNullOrEmpty(board)) {
                profile.setBoard(board.split(","));
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_SYLLABUS) != -1) {
            String syllabus = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_SYLLABUS));
            if (!StringUtil.isNullOrEmpty(syllabus)) {
                profile.setSyllabus(syllabus.split(","));
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_PROFILE_IMAGE) != -1) {
            profile.setProfileImage(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_PROFILE_IMAGE)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_SUBJECT) != -1) {
            String subject = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_SUBJECT));
            if (!StringUtil.isNullOrEmpty(subject)) {
                profile.setSubject(subject.split(","));
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_PROFILE_TYPE) != -1) {
            String profileType = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_PROFILE_TYPE));
            if (profileType.equalsIgnoreCase("student")) {
                profile.setProfileType(ProfileType.STUDENT);
            } else if (profileType.equalsIgnoreCase("teacher")) {
                profile.setProfileType(ProfileType.TEACHER);
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GRADE) != -1) {
            String grade = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GRADE));
            if (!StringUtil.isNullOrEmpty(grade)) {
                profile.setGrade(grade.split(","));
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GRADE_VALUE) != -1) {
            String gradeValue = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_GRADE_VALUE));
            if (!StringUtil.isNullOrEmpty(gradeValue)) {
                profile.setGradeValueMap(GsonUtil.fromJson(gradeValue, Map.class));
            }
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_SOURCE) != -1) {
            String source = cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_SOURCE));
            if (source.equalsIgnoreCase("server")) {
                profile.setSource(UserSource.SERVER);
            } else if (source.equalsIgnoreCase("local")) {
                profile.setSource(UserSource.LOCAL);
            }
        }
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {

        if (!StringUtil.isNullOrEmpty(this.filterCondition)) {
            return this.filterCondition;
        }

        return String.format(Locale.US, "where uid = '%s'", profile.getUid());
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        populateContentValues(contentValues);
        return contentValues;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'",
                ProfileEntry.COLUMN_NAME_UID, profile.getUid());
    }

    private void populateContentValues(ContentValues contentValues) {
        updateFieldsForGroupUser();

        contentValues.put(ProfileEntry.COLUMN_NAME_HANDLE, profile.getHandle());
        contentValues.put(ProfileEntry.COLUMN_NAME_AVATAR, profile.getAvatar());
        contentValues.put(ProfileEntry.COLUMN_NAME_AGE, profile.getAge());
        contentValues.put(ProfileEntry.COLUMN_NAME_GENDER, profile.getGender());
        contentValues.put(ProfileEntry.COLUMN_NAME_STANDARD, profile.getStandard());
        contentValues.put(ProfileEntry.COLUMN_NAME_LANGUAGE, profile.getLanguage());
        contentValues.put(ProfileEntry.COLUMN_NAME_DAY, profile.getDay());
        contentValues.put(ProfileEntry.COLUMN_NAME_MONTH, profile.getMonth());
        contentValues.put(ProfileEntry.COLUMN_NAME_IS_GROUP_USER, getGroupUser());
        contentValues.put(ProfileEntry.COLUMN_NAME_CREATED_AT, profile.getCreatedAt().getTime());

        if (profile.getMedium() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_MEDIUM, StringUtil.join(",", profile.getMedium()));
        }

        if (profile.getBoard() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_BOARD, StringUtil.join(",", profile.getBoard()));
        }

        if (profile.getProfileType() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_PROFILE_TYPE, profile.getProfileType());
        }

        if (profile.getSyllabus() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_SYLLABUS, StringUtil.join(",", profile.getSyllabus()));
        }

        contentValues.put(ProfileEntry.COLUMN_NAME_PROFILE_IMAGE, profile.getProfileImage());
        contentValues.put(ProfileEntry.COLUMN_NAME_PROFILE_TYPE, profile.getProfileType());
        contentValues.put(ProfileEntry.COLUMN_SOURCE, profile.getSource());

        if (profile.getSubject() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_SUBJECT, StringUtil.join(",", profile.getSubject()));
        }

        if (profile.getGrade() != null) {
            contentValues.put(ProfileEntry.COLUMN_NAME_GRADE, StringUtil.join(",", profile.getGrade()));
        }

        if (profile.getGradeValueMap() != null && !profile.getGradeValueMap().isEmpty()) {
            contentValues.put(ProfileEntry.COLUMN_NAME_GRADE_VALUE, GsonUtil.toJson(profile.getGradeValueMap()));
        }

        contentValues.put(ProfileEntry.COLUMN_VALUE, GsonUtil.toJson(profile));
    }

    private void updateFieldsForGroupUser() {
        if (profile.isGroupUser()) {
            profile.setDay(-1);
            profile.setMonth(-1);
            profile.setStandard(-1);
            profile.setAge(-1);
            profile.setGender("");
            profile.setMedium(new String[]{});
            profile.setBoard(new String[]{});
            profile.setProfileImage(profile.getProfileImage());
        }
    }

    private int getGroupUser() {
        return profile.isGroupUser() ? 1 : 0;
    }

    @Override
    public void clean() {
        id = -1L;
        profile = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", ProfileEntry.COLUMN_NAME_UID, profile.getUid());
    }

}
