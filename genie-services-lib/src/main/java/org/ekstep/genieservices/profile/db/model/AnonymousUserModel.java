package org.ekstep.genieservices.profile.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.profile.db.contract.UserEntry;

public class AnonymousUserModel implements IReadable {
    private String uid;
    private AppContext appContext;
    private String customQuery;

    private AnonymousUserModel(AppContext appContext, String customQuery) {
        this.appContext = appContext;
        this.customQuery = customQuery;
    }

    public static AnonymousUserModel findAnonymousUser(AppContext appContext) {
        String query = "select u.uid from users u left join profiles p on p.uid=u.uid where p.uid is null and u.uid is not null";

        AnonymousUserModel anonymousUser = new AnonymousUserModel(appContext, query);
        appContext.getDBSession().read(anonymousUser, query);
        return anonymousUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst())
            uid = cursor.getString(0);
        else
            uid = "";
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
