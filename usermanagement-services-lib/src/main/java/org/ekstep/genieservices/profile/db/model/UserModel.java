package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.UserEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;
import java.util.UUID;

public class UserModel implements IWritable, IReadable, ICleanable {

    private Long id = -1L;
    private IDBSession dbSession;
    private String uid;

    private UserModel(IDBSession dbSession, String uid) {
        this.dbSession = dbSession;
        this.uid = uid;
    }

    // TODO check with Shriharsh why this was required?
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
        contentValues.put(UserEntry.COLUMN_NAME_UID, uid);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        uid = resultSet.getString(resultSet.getColumnIndex(UserEntry.COLUMN_NAME_UID));
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

    @Override
    public void clean() {
        id = -1L;
        uid = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", UserEntry.COLUMN_NAME_UID, uid);
    }

    public String getUid() {
        return uid;
    }
}
