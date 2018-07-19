package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 07/16/2018.
 *
 * @author anil
 */
public class GroupProfilesModel implements IReadable, ICleanable {

    private IDBSession mDBSession;
    private String filterCondition;

    private List<GroupProfileModel> groupProfileModelList;

    private boolean onlyCount;
    private int count;

    private GroupProfilesModel(IDBSession mDBSession, String filterCondition) {
        this.mDBSession = mDBSession;
        this.filterCondition = filterCondition;
    }

    private GroupProfilesModel(IDBSession mDBSession, boolean onlyCount) {
        this.mDBSession = mDBSession;
        this.onlyCount = onlyCount;
    }


    public static GroupProfilesModel find(IDBSession dbSession, String filter) {
        GroupProfilesModel groupProfilesModel = new GroupProfilesModel(dbSession, filter);
        dbSession.read(groupProfilesModel);

        if (groupProfilesModel.getGroupProfileModelList() == null) {
            return null;
        } else {
            return groupProfilesModel;
        }
    }

    public static GroupProfilesModel findByGid(IDBSession dbSession, String gid) {
        String filter = String.format(Locale.US, " where %s = '%s' ", GroupProfileEntry.COLUMN_NAME_GID, gid);

        return find(dbSession, filter);
    }

    public static GroupProfilesModel findByUid(IDBSession dbSession, String uid) {
        String filter = String.format(Locale.US, " where %s = '%s' ", GroupProfileEntry.COLUMN_NAME_UID, uid);

        return find(dbSession, filter);
    }

    public static int count(IDBSession dbSession, String gid) {
        String query = String.format(Locale.US, "select * from %s where %s = '%s';", GroupProfileEntry.TABLE_NAME, GroupProfileEntry.COLUMN_NAME_GID, gid);
        GroupProfilesModel model = new GroupProfilesModel(dbSession, true);
        dbSession.read(model, query);
        return model.count;
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            groupProfileModelList = new ArrayList<>();

            do {
                GroupProfileModel groupProfileModel = GroupProfileModel.build(mDBSession);

                groupProfileModel.readWithoutMoving(resultSet);

                groupProfileModelList.add(groupProfileModel);
            } while (resultSet.moveToNext());
        }

        return this;
    }

    @Override
    public String getTableName() {
        return GroupProfileEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        groupProfileModelList = null;
    }

    @Override
    public String selectionToClean() {
        return filterCondition;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", GroupProfileEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
    }

    @Override
    public String filterForRead() {
        return filterCondition;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<GroupProfileModel> getGroupProfileModelList() {
        return groupProfileModelList;
    }

}
