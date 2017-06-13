package org.ekstep.genieservices.importexport.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class MetadataModel implements IReadable, IWritable {

    private Long id = -1L;
    private IDBSession dbSession;
    private Map<String, Object> metadata;
    private String key;
    private String value;

    private MetadataModel(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    private MetadataModel(IDBSession dbSession, String key, String value) {
        this(dbSession);

        this.key = key;
        this.value = value;
    }

    public static MetadataModel findAll(IDBSession dbSession) {
        MetadataModel model = new MetadataModel(dbSession);
        dbSession.read(model);

        if (model.metadata == null) {
            return null;
        } else {
            return model;
        }
    }

    public static MetadataModel build(IDBSession dbSession, String key, String value) {
        return new MetadataModel(dbSession, key, value);
    }

    public Void save() {
        dbSession.create(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            metadata = new HashMap<>();
            do {
                metadata.put(resultSet.getString(resultSet.getColumnIndex(MetaEntry.COLUMN_NAME_KEY)), resultSet.getString(resultSet.getColumnIndex(MetaEntry.COLUMN_NAME_VALUE)));
            } while (resultSet.moveToNext());
        }
        return this;
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
        return new String[0];
    }

    @Override
    public String limitBy() {
        return "";
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MetaEntry.COLUMN_NAME_KEY, key);
        contentValues.put(MetaEntry.COLUMN_NAME_VALUE, value);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return MetaEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
