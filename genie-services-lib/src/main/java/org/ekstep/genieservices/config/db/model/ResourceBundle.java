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
import org.ekstep.genieservices.config.db.contract.ResourceBundleEntry;

import java.sql.ResultSet;
import java.util.Locale;

public class ResourceBundle implements IWritable, IReadable, IUpdatable, ICleanable {
    private static final String TAG = "model-ResourceBundle";
    private String mIdentifier;
    private String mJson;
    private Long id = -1L;
    private AppContext mAppContext;

    private ResourceBundle(AppContext appContext, String identifier) {
        mAppContext = appContext;
        mIdentifier = identifier;
    }

    private ResourceBundle(AppContext appContext, String identifier, String json) {
        mAppContext = appContext;
        mJson = json;
        mIdentifier = identifier;
    }

    public static ResourceBundle create(AppContext appContext, String identifier, String json) {
        return new ResourceBundle(appContext, identifier, json);
    }

    public static ResourceBundle findById(AppContext appContext, String identifier) {
        ResourceBundle resourceBundle = new ResourceBundle(appContext, identifier);
        appContext.getDBSession().read(resourceBundle);
        return resourceBundle;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ResourceBundleEntry.COLUMN_NAME_BUNDLE_IDENTIFIER, mIdentifier);
        contentValues.put(ResourceBundleEntry.COLUMN_NAME_BUNDLE_JSON, mJson.getBytes());
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ResourceBundleEntry.COLUMN_NAME_BUNDLE_JSON, mJson);
        return contentValues;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            readByMovingToFirst(resultSet);
        return this;
    }

    /**
     * Moves the resultset to the top and reads
     *
     * @param resultSet
     */
    private void readByMovingToFirst(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            id = resultSet.getLong(resultSet.getColumnIndex(BaseColumns._ID));
            mIdentifier = resultSet.getString(resultSet.getColumnIndex(ResourceBundleEntry.COLUMN_NAME_BUNDLE_IDENTIFIER));
            byte[] bytes = resultSet.getBlob(resultSet.getColumnIndex(ResourceBundleEntry.COLUMN_NAME_BUNDLE_JSON));
            mJson = new String(bytes);
        } else {
            mIdentifier = "";
        }
    }

    @Override
    public String getTableName() {
        return ResourceBundleEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public void clean() {
        id = -1L;
        mIdentifier = null;
        mJson = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", ResourceBundleEntry.COLUMN_NAME_BUNDLE_IDENTIFIER, mIdentifier);
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", ResourceBundleEntry.COLUMN_NAME_BUNDLE_IDENTIFIER, mIdentifier);
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        mAppContext.getLogger().info(TAG, String.format("SEARCH Bundle: %s", mIdentifier));
        String selectionCriteria = String.format(Locale.US, "where %s = '%s'", ResourceBundleEntry.COLUMN_NAME_BUNDLE_IDENTIFIER, mIdentifier);
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

    public void save() {
        mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                context.getDBSession().clean(ResourceBundle.this);
                context.getDBSession().create(ResourceBundle.this);
                return null;
            }
        });
    }

    public String getResourceString() {
        return mJson;
    }

    public boolean exists() {
        return this.id != null && this.id != -1L;
    }
}
