package org.ekstep.genieservices.commons.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.contract.NoSqlEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created by swayangjit on 10/9/17.
 */

public class NoSqlModel implements IWritable, IReadable, IUpdatable, ICleanable {

    private Long id = -1L;
    private IDBSession mDBSession;
    private String mKey;
    private String mValue;


    private NoSqlModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private NoSqlModel(IDBSession dbSession, String key) {
        this.mDBSession = dbSession;
        this.mKey = key;
    }

    private NoSqlModel(IDBSession dbSession, String key, String value) {
        this.mDBSession = dbSession;
        this.mKey = key;
        this.mValue = value;
    }

    public static NoSqlModel build(IDBSession dbSession) {
        return new NoSqlModel(dbSession);
    }

    public static NoSqlModel build(IDBSession dbSession, String key, String value) {
        return new NoSqlModel(dbSession, key, value);
    }

    public static NoSqlModel findByKey(IDBSession dbSession, String key) {
        NoSqlModel noSqlModel = new NoSqlModel(dbSession, key);
        dbSession.read(noSqlModel);
        return noSqlModel.getValue() == null ? null : noSqlModel;

    }

    public static NoSqlModel findWithCustomQuery(IDBSession dbSession, String query) {
        NoSqlModel noSqlModel = new NoSqlModel(dbSession, null);
        dbSession.read(noSqlModel, query);

        if (noSqlModel.getValue() == null) {
            return null;
        } else {
            return noSqlModel;
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoSqlEntry.COLUMN_NAME_KEY, mKey);
        contentValues.put(NoSqlEntry.COLUMN_NAME_VALUE, mValue);
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
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoSqlEntry.COLUMN_NAME_VALUE, mValue);
        return contentValues;
    }

    @Override
    public String getTableName() {
        return NoSqlEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        id = -1L;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", NoSqlEntry.COLUMN_NAME_KEY, mKey);
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", NoSqlEntry.COLUMN_NAME_KEY, mKey);
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = '%s'", NoSqlEntry.COLUMN_NAME_KEY, mKey);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void save() {
        mDBSession.create(this);
    }

    public void update() {
        mDBSession.update(this);
    }

    public void delete() {
        mDBSession.clean(this);
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        mKey = resultSet.getString(resultSet.getColumnIndex(NoSqlEntry.COLUMN_NAME_KEY));
        mValue = resultSet.getString(resultSet.getColumnIndex(NoSqlEntry.COLUMN_NAME_VALUE));
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

}
