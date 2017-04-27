package org.ekstep.genieservices.config.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.config.db.contract.OrdinalsEntry;

import java.util.Locale;

/**
 * Created on 4/21/2017.
 *
 * @author anil
 */
public class OrdinalsModel implements IReadable, IWritable, IUpdatable, ICleanable {

    private static final String TAG = "model-Ordinals";

    private AppContext mAppContext;

    private Long id = -1L;
    private String mIdentifier;
    private String mJson;

    private OrdinalsModel(AppContext appContext, String identifier) {
        mAppContext = appContext;
        this.mIdentifier = identifier;
    }

    private OrdinalsModel(AppContext appContext, String identifier, String json) {
        mAppContext = appContext;
        this.mJson = json;
        this.mIdentifier = identifier;
    }

    public static OrdinalsModel create(AppContext appContext, String identifier, String json) {
        return new OrdinalsModel(appContext, identifier, json);
    }

    public static OrdinalsModel findById(AppContext appContext, String identifier) {
        OrdinalsModel ordinals = new OrdinalsModel(appContext, identifier);
        appContext.getDBSession().read(ordinals);
        return ordinals;
    }

    public boolean exists() {
        return this.id != null && this.id != -1L;
    }

    public void save() {
        mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                context.getDBSession().clean(OrdinalsModel.this);
                context.getDBSession().create(OrdinalsModel.this);
                return null;
            }
        });
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
        Logger.i(mAppContext, TAG, String.format("SEARCH Ordinals: %s", mIdentifier));

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
