package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;

import java.util.Date;
import java.util.Locale;

public class UserProfileModel implements IWritable, IReadable, IUpdatable, ICleanable {

    private long id = -1;
    private Profile profile;
    private IDBSession dbSession;

    private UserProfileModel(IDBSession dbSession, Profile profile) {
        this.profile = profile;
        this.dbSession = dbSession;
    }

    public static UserProfileModel buildUserProfile(IDBSession dbSession, Profile profile) {
        UserProfileModel profileModel = new UserProfileModel(dbSession, profile);
        return profileModel;
    }

    public static UserProfileModel findUserProfile(IDBSession dbSession, String uid) {
        Profile profile = new Profile(uid);
        UserProfileModel profileModel = new UserProfileModel(dbSession, profile);
        dbSession.read(profileModel);
        //check if the profile was found and return null if the profile was not found
        if (StringUtil.isNullOrEmpty(profileModel.profile.getHandle())) {
            return null;
        } else {
            return profileModel;
        }
    }

    public long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
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
        /**
         * The column index checks below are required because we allow exporting and importing profiles and profiles from previous Genie versions
         * may not have some of these columns.
         */
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
            profile.setMedium(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_MEDIUM)));
        }

        if (cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_BOARD) != -1) {
            profile.setBoard(cursor.getString(cursor.getColumnIndex(ProfileEntry.COLUMN_NAME_BOARD)));
        }
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
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
        return String.format(Locale.US, "uid = '%s'", profile.getUid());
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
        contentValues.put(ProfileEntry.COLUMN_NAME_MEDIUM, profile.getMedium());
        contentValues.put(ProfileEntry.COLUMN_NAME_BOARD, profile.getBoard());
    }

    private void updateFieldsForGroupUser() {
        if (profile.isGroupUser()) {
            profile.setDay(-1);
            profile.setMonth(-1);
            profile.setStandard(-1);
            profile.setAge(-1);
            profile.setGender("");
            profile.setMedium("");
            profile.setBoard("");
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
