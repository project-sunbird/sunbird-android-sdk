package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

/**
 * profile
 * Created on 13/7/18.
 * shriharsh
 */
public class GroupModel implements IWritable, IReadable, IUpdatable, ICleanable {

    private Long id = -1L;
    private IDBSession mDBSession;
    private Group mGroup;

    private GroupModel(IDBSession dBSession, Group group) {
        this.mDBSession = dBSession;
        this.mGroup = group;
    }

    public static GroupModel build(IDBSession dbSession, Group group) {
        GroupModel groupModel = new GroupModel(dbSession, group);
        return groupModel;
    }

    public static GroupModel findGroupById(IDBSession dbSession, String gid) {
        //create group with empty name
        Group group = new Group("");
        group.setGid(gid);

        GroupModel groupModel = new GroupModel(dbSession, group);
        dbSession.read(groupModel);

        //check if the id is -1
        if (groupModel.id == -1) {
            return null;
        } else {
            return groupModel;
        }
    }

    public void update() {
        mDBSession.update(this);
    }

    public Void save() {
        mDBSession.create(this);
        return null;
    }

    public Void delete() {
        mDBSession.clean(this);
        return null;
    }


    @Override
    public IReadable read(IResultSet resultSet) {
        return null;
    }

    @Override
    public String orderBy() {
        return null;
    }

    @Override
    public String filterForRead() {
        return null;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return null;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        return null;
    }

    @Override
    public String updateBy() {
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public void updateId(long id) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return null;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public Group getGroup() {
        return mGroup;
    }
}
