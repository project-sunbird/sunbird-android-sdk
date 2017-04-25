package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.content.db.model.ContentAccess;
import org.ekstep.genieservices.profile.db.contract.UserEntry;

import java.util.Locale;
import java.util.UUID;

public class User implements IWritable, IReadable, ICleanable {
    private String uid;
    private Long id = -1L;
    private AppContext mAppContext;
    private Profile profile = null;

    private User(AppContext appContext) {
        this(appContext, UUID.randomUUID().toString(), null);
    }

    private User(AppContext appContext, String uid) {
        this(appContext, uid, null);
    }

    private User(AppContext appContext, Profile profile) {
        this(appContext, UUID.randomUUID().toString(), profile);
    }

    private User(AppContext appContext, String uid, Profile profile) {
        this.mAppContext = appContext;
        this.uid = uid;
        this.profile = profile;
    }

    public static User buildUser(AppContext appContext, Profile profile) {
        User user = new User(appContext, profile);
        return user;
    }

    public static User buildUser(AppContext appContext) {
        User user = new User(appContext);
        return user;
    }

    public static User findByUserId(AppContext appContext, String uid) {
        User user = new User(appContext, uid);
        appContext.getDBSession().read(user);
        return user;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(UserEntry.COLUMN_NAME_UID, uid);
        return contentValues;
    }

    @Override
    public void updateId(long id) {

    }

    public void readAfterMoving(IResultSet cursor, boolean movedToFirst) {
        if (cursor != null && movedToFirst) {
            id = cursor.getLong(0);
            uid = cursor.getString(1);
        } else
            uid = "";
    }

    public boolean exists() {
        return (uid != null && !uid.isEmpty());
    }

    @Override
    public IReadable read(IResultSet cursor) {
        boolean movedToFirst = cursor != null && cursor.moveToFirst();
        readAfterMoving(cursor, movedToFirst);
        return this;
    }

    @Override
    public String getTableName() {
        return UserEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        String selectionCriteria = String.format(Locale.US, "where uid = '%s'", uid);
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

    public void create(String gameID, String gameVersion, String location,
                       IDeviceInfo deviceInfo) {

        mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                context.getDBSession().create(User.this);

                // TODO: 24/4/17 Should add telemetry event after creating a new user

                if (profile != null) {
                    profile.setUid(uid);
                    UserProfile profileDTO = UserProfile.buildUserProfile(mAppContext, profile);
                    context.getDBSession().create(profileDTO);

                    // TODO: 24/4/17 Should add telemetry event after creating ProfileDTO
                }
                return null;
            }
        });


//        GECreateUser geCreateUser = generateGeCreateUserEvent(gameID, gameVersion, location, deviceInfo);
//        Set<String> hashedGenieTags = TelemetryTagCache.activeTags(dbOperator, context);
//        Event userEvent = new Event(geCreateUser.getEID(), hashedGenieTags).withEvent(geCreateUser.toString());
//        tasks.add(new Writer(userEvent));

//        if (profile != null) {
//            profile.setUid(uid);
//            ProfileDTO profileDTO = new ProfileDTO(profile);
//            tasks.add(new Writer(profileDTO));
//            GECreateProfile geCreateProfile = generateGeCreateProfileEvent(gameID, gameVersion, location, deviceInfo);
//            Event profileEvent = new Event(geCreateProfile.getEID(), hashedGenieTags).withEvent(geCreateProfile.toString());
//            tasks.add(new Writer(profileEvent));
//        }
//        dbOperator.executeInOneTransaction(tasks);
    }

//    @NonNull
//    protected GECreateProfile generateGeCreateProfileEvent(String gameID, String gameVersion, String location, DeviceInfo deviceInfo) {
//        GECreateProfile geCreateProfile = new GECreateProfile(gameID, gameVersion, profile, location);
//        geCreateProfile.setDid(deviceInfo.getDeviceID());
//        geCreateProfile.setTs(TimeUtil.getCurrentTimestamp());
//        return geCreateProfile;
//    }

//    @NonNull
//    protected GECreateUser generateGeCreateUserEvent(String gameID, String gameVersion, String location, DeviceInfo deviceInfo) {
//        GECreateUser geCreateUser = new GECreateUser(gameID, gameVersion, this.getUid(), location);
//        geCreateUser.setDid(deviceInfo.getDeviceID());
//        geCreateUser.setTs(TimeUtil.getCurrentTimestamp());
//        return geCreateUser;
//    }

//    public void initialize(DbOperator dbOperator) {
//        Reader reader = new Reader(this);
//        dbOperator.execute(reader);
//    }

    private User getExistingUser(String uid) {
        User otherUser = User.findByUserId(mAppContext, uid);
        return otherUser;
    }

//    public Void save(DbOperator dbOperator) {
//        Writer writer = new Writer(this);
//        dbOperator.execute(writer);
//        return null;
//    }

    public void delete(final AppContext appContext, IDeviceInfo deviceInfo, String gameID, String versionName) {
//        List<IOperate> tasks = new ArrayList<>();
        Profile profile = new Profile("", "", "");
        profile.setUid(uid);

        final UserProfile userProfile = UserProfile.buildUserProfile(appContext, profile);
//        tasks.add(new Cleaner(userProfile));

        final User existingUser = getExistingUser(uid);
//        tasks.add(new Cleaner(existingUser));

        // Delete the rows for uid
        final ContentAccess contentAccess = ContentAccess.buildContentAccess(appContext, uid, null);
//        tasks.add(new Cleaner(contentAccess));

        // TODO: 25/4/17 Need to add a telemetry event
//        Set<String> hashedGenieTags = TelemetryTagCache.activeTags(dbOperator, context);
//        GEDeleteProfile geDeleteProfile = generateGeDeleteProfileEvent(profile, deviceInfo, gameID, versionName);
//        Event deleteProfileEvent = new Event(geDeleteProfile.getEID(), hashedGenieTags).withEvent(geDeleteProfile.toString());
//        tasks.add(new Writer(deleteProfileEvent));

        appContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                appContext.getDBSession().clean(userProfile);
                appContext.getDBSession().clean(existingUser);
                appContext.getDBSession().clean(contentAccess);

                return null;
            }
        });
    }

//    private GEDeleteProfile generateGeDeleteProfileEvent(Profile profile, DeviceInfo deviceInfo, String gameID, String versionName) {
//        GEDeleteProfile geDeleteProfile = new GEDeleteProfile(profile, gameID, versionName);
//        geDeleteProfile.setDid(deviceInfo.getDeviceID());
//        return geDeleteProfile;
//    }


    @Override
    public void clean() {
        id = -1L;
        uid = null;
    }


    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", UserEntry.COLUMN_NAME_UID, getUid());
    }


}
