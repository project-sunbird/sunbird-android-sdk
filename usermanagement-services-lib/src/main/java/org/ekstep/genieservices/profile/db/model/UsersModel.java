package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.db.contract.UserEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/9/2017.
 *
 * @author anil
 */
public class UsersModel implements IReadable {

    private IDBSession dbSession;
    private List<UserModel> userModelList;

    private UsersModel(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    public static UsersModel findAll(IDBSession dbSession) {
        UsersModel model = new UsersModel(dbSession);
        dbSession.read(model);

        if (model.userModelList == null) {
            return null;
        } else {
            return model;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            userModelList = new ArrayList<>();
            do {
                UserModel userModel = UserModel.build(dbSession);
                userModel.readWithoutMoving(resultSet);

                userModelList.add(userModel);
            } while (resultSet.moveToNext());
        }
        return null;
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
        return "";
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
    }
}
