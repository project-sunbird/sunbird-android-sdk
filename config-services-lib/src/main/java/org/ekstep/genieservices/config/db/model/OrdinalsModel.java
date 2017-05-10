package org.ekstep.genieservices.config.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.contract.OrdinalsEntry;

import java.util.Locale;

/**
 * Created on 4/21/2017.
 *
 * @author anil
 */
public class OrdinalsModel implements IReadable, IWritable, IUpdatable, ICleanable {

    private static final String TAG = "model-Ordinals";

    private IDBSession mDBSession;

    private Long id = -1L;
    private String mIdentifier;
    private String mJson;

    private OrdinalsModel(IDBSession dbSession, String identifier) {
        this.mDBSession = dbSession;
        this.mIdentifier = identifier;
    }

    private OrdinalsModel(IDBSession dbSession, String identifier, String json) {
        this.mDBSession = dbSession;
        this.mJson = json;
        this.mIdentifier = identifier;
    }

    public static OrdinalsModel build(IDBSession dbSession, String identifier, String json) {
        return new OrdinalsModel(dbSession, identifier, json);
    }

    public static OrdinalsModel findById(IDBSession dbSession, String identifier) {
        OrdinalsModel ordinals = new OrdinalsModel(dbSession, identifier);
        dbSession.read(ordinals);
        if (ordinals.getJSON() == null) {
            return null;
        } else {
            return ordinals;
        }
    }

    public boolean exists() {
        return this.id != null && this.id != -1L;
    }

    public void save() {
        mDBSession.create(this);
    }

    public void update() {
        mDBSession.update(this);
    }

    public String getJSON() {
        return mJson;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            readByMovingToFirst(resultSet);
        return this;
    }

    private void readByMovingToFirst(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
            mIdentifier = resultSet.getString(resultSet.getColumnIndex(OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER));
            byte[] bytes = resultSet.getBlob(resultSet.getColumnIndex(OrdinalsEntry.COLUMN_NAME_ORDINAL_JSON));
            mJson = new String(bytes);
        } else {
            mIdentifier = "";
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER, mIdentifier);
        contentValues.put(OrdinalsEntry.COLUMN_NAME_ORDINAL_JSON, mJson.getBytes());
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER, mIdentifier);
        return contentValues;
    }

    @Override
    public String getTableName() {
        return OrdinalsEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        id = -1L;
        mIdentifier = null;
        mJson = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER, mIdentifier);
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER, mIdentifier);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        String selectionCriteria = String.format(Locale.US, "where %s = '%s'", OrdinalsEntry.COLUMN_NAME_ORDINAL_IDENTIFIER, mIdentifier);
        return selectionCriteria;
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
