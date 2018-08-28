package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.db.contract.GroupEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Locale;
import java.util.Map;

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
        return new GroupModel(dbSession, group);
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
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    public void readWithoutMoving(IResultSet cursor) {
        id = cursor.getLong(0);

        //gid
        mGroup.setGid(cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GID)));

        //name
        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GROUP_NAME) != -1) {
            mGroup.setName(cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GROUP_NAME)));
        }

        //syllabus
        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_SYLLABUS) != -1) {
            String syllabus = cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_SYLLABUS));
            if (!StringUtil.isNullOrEmpty(syllabus)) {
                mGroup.setSyllabus(syllabus.split(","));
            }
        }

        //grade
        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GRADE) != -1) {
            String grade = cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GRADE));
            if (!StringUtil.isNullOrEmpty(grade)) {
                mGroup.setGrade(grade.split(","));
            }
        }

        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GRADE_VALUE) != -1) {
            String gradeValue = cursor.getString(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_GRADE_VALUE));
            if (!StringUtil.isNullOrEmpty(gradeValue)) {
                mGroup.setGradeValueMap(GsonUtil.fromJson(gradeValue, Map.class));
            }
        }

        //createdAt
        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_CREATED_AT) != -1) {
            mGroup.setCreatedAt(cursor.getLong(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_CREATED_AT)));
        }

        //updatedAt
        if (cursor.getColumnIndex(GroupEntry.COLUMN_NAME_UPDATED_AT) != -1) {
            mGroup.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(GroupEntry.COLUMN_NAME_UPDATED_AT)));
        }

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where gid = '%s'", mGroup.getGid());
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
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        populateContentValues(contentValues);
        return contentValues;
    }

    private void populateContentValues(ContentValues contentValues) {
        contentValues.put(GroupEntry.COLUMN_NAME_GID, mGroup.getGid());
        contentValues.put(GroupEntry.COLUMN_NAME_GROUP_NAME, mGroup.getName());

        if (mGroup.getSyllabus() != null) {
            contentValues.put(GroupEntry.COLUMN_NAME_SYLLABUS, StringUtil.join(",", mGroup.getSyllabus()));
        }

        if (mGroup.getGrade() != null) {
            contentValues.put(GroupEntry.COLUMN_NAME_GRADE, StringUtil.join(",", mGroup.getGrade()));
        }

        if (mGroup.getGradeValueMap() != null && !mGroup.getGradeValueMap().isEmpty()) {
            contentValues.put(GroupEntry.COLUMN_NAME_GRADE_VALUE, GsonUtil.toJson(mGroup.getGradeValueMap()));
        }

        contentValues.put(GroupEntry.COLUMN_NAME_CREATED_AT, mGroup.getCreatedAt());
        contentValues.put(GroupEntry.COLUMN_NAME_UPDATED_AT, mGroup.getUpdatedAt());
    }


    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'",
                GroupEntry.COLUMN_NAME_GID, mGroup.getGid());
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupEntry.COLUMN_NAME_GID, mGroup.getGid());
        populateContentValues(contentValues);

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return GroupEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        id = -1L;
        mGroup = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", GroupEntry.COLUMN_NAME_GID, mGroup.getGid());
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public Group getGroup() {
        return mGroup;
    }
}
