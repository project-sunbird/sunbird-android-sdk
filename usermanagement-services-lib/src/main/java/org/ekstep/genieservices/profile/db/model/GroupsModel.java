package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.db.contract.GroupEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 17/7/18.
 * shriharsh
 */
public class GroupsModel implements IReadable {

    private IDBSession mDBSession;
    private List<Group> mGroupsList;

    private GroupsModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    public static GroupsModel find(IDBSession dbSession) {
        GroupsModel groupsModel = new GroupsModel(dbSession);
        dbSession.read(groupsModel);
        if (groupsModel.getGroupList() == null) {
            return null;
        } else {
            return groupsModel;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            mGroupsList = new ArrayList<>();

            do {
                GroupModel groupModel = GroupModel.build(mDBSession, new Group(""));

                groupModel.readWithoutMoving(resultSet);

                Group group = groupModel.getGroup();

                int profilesCount = GroupProfilesModel.count(mDBSession, group.getGid());
                group.setProfilesCount(profilesCount);

                mGroupsList.add(group);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public String getTableName() {
        return GroupEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, "order by %s asc", GroupEntry.COLUMN_NAME_GROUP_NAME);
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

    public List<Group> getGroupList() {
        return mGroupsList;
    }

}
