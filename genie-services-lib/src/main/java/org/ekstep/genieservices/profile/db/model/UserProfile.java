package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.profile.db.contract.ProfileEntry;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class UserProfile implements IWritable, IReadable, IUpdatable, ICleanable {

    private long id = -1;
    private Profile profile;
    private AppContext appContext;
    private boolean isMigration_02;

    private UserProfile(AppContext appContext, Profile profile) {
        this.profile = profile;
        this.appContext = appContext;

        if (profile.getUid() == null || profile.getUid().isEmpty()) {
            generateUID();
        }

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(new Date());
        }

    }

    public static UserProfile buildUserProfile(AppContext appContext, Profile profile) {
        UserProfile profileDTO = new UserProfile(appContext, profile);
        return profileDTO;
    }

    public long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    private void generateUID() {
        String uid = UUID.randomUUID().toString();
        profile.setUid(uid);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
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
        if (cursor == null) {
            return;
        }

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

        // In _07_UIBrandingMigration introducing two new col medium and board.
        // Following check is required while importing profile before this migration.
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
        String selectionCriteria = String.format(Locale.US, "where uid = '%s'", profile.getUid());
        return selectionCriteria;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

//    public void initialize(DbOperator dbOperator) {
//        Reader reader = new Reader(this);
//        dbOperator.execute(reader);
//    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();

        populateContentValues(contentValues);

        return contentValues;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "uid = '%s'", profile.getUid());
    }

    public void update(String gameID, String gameVersion, IDeviceInfo deviceInfo, AppContext appContext) {
//        List<IOperate> tasks = new ArrayList<>();
//        tasks.add(new Updater(this));

//        GEUpdateProfile geUpdateProfile = new GEUpdateProfile(gameID, gameVersion, profile, deviceInfo.getDeviceID());
//        Event profileEvent = new Event(geUpdateProfile.getEID(), TelemetryTagCache.activeTags(dbOperator, context)).withEvent(geUpdateProfile.toString());
//
//        tasks.add(new Writer(profileEvent));
        // TODO: 26/4/17 GEUpdateEvent has to be added to the telemetry
        appContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                context.getDBSession().update(UserProfile.this);
                return null;
            }
        });

//        dbOperator.executeInOneTransaction(tasks);
    }

//    public Void create(DeviceDetails deviceDetails, DbOperator dbOperator) {
//        Event event = generateGeCreateProfileEvent(deviceDetails.getGameID(), deviceDetails.getGameVersion(), deviceDetails.getLocation(), deviceDetails.getDeviceInfo(), deviceDetails.getActiveGenieTags());
//        Writer profileWriter = new Writer(this);
//        Writer createProfileEventWriter = new Writer(event);
//
//        List<IOperate> dbOperations = new ArrayList<>();
//        dbOperations.add(profileWriter);
//        dbOperations.add(createProfileEventWriter);
//
//        dbOperator.executeInOneTransaction(dbOperations);
//
//        return null;
//    }

//    public boolean exists(DbOperator dbOperator) {
//        String uid = getProfile().getUid();
//        Profile profile = new Profile("", "", "");
//        profile.setUid(uid);
//
//        ProfileDTO otherProfileDTO = getOtherProfileDTO(dbOperator, profile);
//
//        return otherProfileDTO.getId() != -1;
//    }

//    @NonNull
//    protected ProfileDTO getOtherProfileDTO(DbOperator dbOperator, Profile profile) {
//        ProfileDTO otherProfileDTO = new ProfileDTO(profile);
//        Reader reader = new Reader(otherProfileDTO);
//        dbOperator.execute(reader);
//
//        return otherProfileDTO;
//    }

    public void enableMigration_02() {
        isMigration_02 = true;
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

        if (!isMigration_02) {
            contentValues.put(ProfileEntry.COLUMN_NAME_MEDIUM, profile.getMedium());
        }

        if (!isMigration_02) {
            contentValues.put(ProfileEntry.COLUMN_NAME_BOARD, profile.getBoard());
        }
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

    protected void updateWithCurrentTime() {
        profile.setCreatedAt(new Date());
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

//    protected Event generateGeCreateProfileEvent(String gameID, String gameVersion, String location, DeviceInfo deviceInfo, Set<String> hashedTags) {
//        GECreateProfile geCreateProfile = getGeCreateProfile(gameID, gameVersion, location, deviceInfo);
//
//        return new Event(geCreateProfile.getEID(), hashedTags).withEvent(geCreateProfile.toString());
//    }
//
//    @NonNull
//    private GECreateProfile getGeCreateProfile(String gameID, String gameVersion, String location, DeviceInfo deviceInfo) {
//        GECreateProfile geCreateProfile = new GECreateProfile(gameID, gameVersion, profile, location);
//        geCreateProfile.setDid(deviceInfo.getDeviceID());
//        geCreateProfile.setTs(TimeUtil.getCurrentTimestamp());
//
//        return geCreateProfile;
//    }

}
