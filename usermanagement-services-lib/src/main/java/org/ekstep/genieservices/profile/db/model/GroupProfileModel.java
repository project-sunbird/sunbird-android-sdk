package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Locale;

/**
 *
 */
public class GroupProfileModel implements IWritable, IReadable, ICleanable {

    private IDBSession mDBSession;

    private Long id = -1L;
    private String gid;
    private String uid;

    private GroupProfileModel(IDBSession dBSession) {
        this.mDBSession = dBSession;
    }

    private GroupProfileModel(IDBSession dBSession, String gid, String uid) {
        this.mDBSession = dBSession;
        this.gid = gid;
        this.uid = uid;
    }

    public static GroupProfileModel build(IDBSession dbSession) {
        return new GroupProfileModel(dbSession);
    }

    public static GroupProfileModel build(IDBSession dbSession, String gid, String uid) {
        return new GroupProfileModel(dbSession, gid, uid);
    }

    public static GroupProfileModel find(IDBSession dbSession, String gid, String uid) {
        return new GroupProfileModel(dbSession, gid, uid);
    }

    public void save() {
        mDBSession.create(this);
    }

    public void delete() {
        mDBSession.clean(this);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupProfileEntry.COLUMN_NAME_GID, this.gid);
        contentValues.put(GroupProfileEntry.COLUMN_NAME_UID, this.uid);
        contentValues.put(GroupProfileEntry.COLUMN_NAME_EPOCH_TIMESTAMP, DateUtil.getEpochTime());

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }

        return this;
    }

    @Override
    public String getTableName() {
        return GroupProfileEntry.TABLE_NAME;
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return null;
    }

    @Override
    public String orderBy() {
        return String.format(Locale.US, " order by %s desc", GroupProfileEntry.COLUMN_NAME_EPOCH_TIMESTAMP);
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ? AND %s = ?", GroupProfileEntry.COLUMN_NAME_GID, GroupProfileEntry.COLUMN_NAME_UID);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{gid, uid};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        gid = resultSet.getString(resultSet.getColumnIndex(GroupProfileEntry.COLUMN_NAME_GID));
        uid = resultSet.getString(resultSet.getColumnIndex(GroupProfileEntry.COLUMN_NAME_UID));
//        epochTimestamp = resultSet.getLong(resultSet.getColumnIndex(GroupProfileEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
    }

    public String getGid() {
        return gid;
    }

    public String getUid() {
        return uid;
    }
}
