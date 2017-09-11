package org.ekstep.genieservices.commons.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.contract.KeyValueStoreEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created by swayangjit on 10/9/17.
 */

public class KeyValueStoreModel implements IWritable, IReadable {

    private Long id = -1L;
    private IDBSession mDBSession;
    private String mKey;
    private String mValue;

    private KeyValueStoreModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private KeyValueStoreModel(IDBSession dbSession, String key) {
        this.mDBSession = dbSession;
        this.mKey = key;
    }

    private KeyValueStoreModel(IDBSession dbSession, String key, String value) {
        this.mDBSession = dbSession;
        this.mKey = key;
        this.mValue = value;
    }

    public static KeyValueStoreModel build(IDBSession dbSession, String key, String value) {
        return new KeyValueStoreModel(dbSession, key, value);
    }

    public static KeyValueStoreModel findByKey(IDBSession dbSession, String key) {
        KeyValueStoreModel keyStoreModel = new KeyValueStoreModel(dbSession, key);
        dbSession.read(keyStoreModel);
        return keyStoreModel.getValue() == null ? null : keyStoreModel;

    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KeyValueStoreEntry.COLUMN_NAME_KEY, mKey);
        contentValues.put(KeyValueStoreEntry.COLUMN_NAME_VALUE, mValue.getBytes());
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
        return KeyValueStoreEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = '%s'", KeyValueStoreEntry.COLUMN_NAME_KEY, mKey);
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

    private void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
        mKey = resultSet.getString(resultSet.getColumnIndex(KeyValueStoreEntry.COLUMN_NAME_KEY));
        byte[] bytes = resultSet.getBlob(resultSet.getColumnIndex(KeyValueStoreEntry.COLUMN_NAME_VALUE));
        mValue = new String(bytes);
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }

}
