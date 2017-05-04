package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.profile.db.contract.UserEntry;

public class AnonymousUserModel implements IReadable {
    private String uid;
    private IDBSession dbSession;
    private String customQuery;

    private AnonymousUserModel(IDBSession dbSession, String customQuery) {
        this.dbSession = dbSession;
        this.customQuery = customQuery;
    }

    public static AnonymousUserModel findAnonymousUser(IDBSession dbSession) {
        String query = "select u.uid from users u left join profiles p on p.uid=u.uid where p.uid is null and u.uid is not null";
        AnonymousUserModel anonymousUser = new AnonymousUserModel(dbSession, query);
        dbSession.read(anonymousUser, query);
        if (anonymousUser.uid == null) {
            return null;
        } else {
            return anonymousUser;
        }
    }

    public String getUid() {
        return uid;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst())
            uid = cursor.getString(0);
        return this;
    }

    @Override
    public String getTableName() {
        return UserEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

}
