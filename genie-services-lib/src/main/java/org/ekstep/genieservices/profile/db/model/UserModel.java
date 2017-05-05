package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.content.db.model.ContentAccessModel;
import org.ekstep.genieservices.profile.db.contract.UserEntry;

import java.util.Locale;
import java.util.UUID;

public class UserModel implements IWritable, IReadable, ICleanable {
    private String uid;
    private Long id = -1L;
    private IDBSession dbSession;

    private UserModel(IDBSession dbSession, String uid) {
        this.dbSession = dbSession;
        this.uid = uid;
    }

    public static UserModel build(IDBSession dbSession) {
        UserModel user = new UserModel(dbSession, UUID.randomUUID().toString());
        return user;
    }

    public static UserModel build(IDBSession dbSession, String uid) {
        UserModel user = new UserModel(dbSession, uid);
        return user;
    }

    public static UserModel findByUserId(IDBSession dbSession, String uid) {
        UserModel user = new UserModel(dbSession, uid);
        dbSession.read(user);
        // return null if the user was not found
        if (user.id == -1) {
            return null;
        } else {
            return user;
        }
    }

    public String getUid() {
        return uid;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME_UID, uid);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    public void readAfterMoving(IResultSet cursor) {
        id = cursor.getLong(0);
        uid = cursor.getString(1);
    }

    public boolean exists() {
        return (uid != null && !uid.isEmpty());
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst())
            readAfterMoving(cursor);
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
        return String.format(Locale.US, "where uid = '%s'", uid);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public void save() {
        dbSession.create(this);

                //TODO: THe below telemetry logging should be part of the service and not in the model. Model should only handle DB interactions
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

    public void delete() {
        dbSession.clean(this);
    }

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
