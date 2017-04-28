package org.ekstep.genieservices.config.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.config.db.contract.MasterDataEntry;

import java.util.Locale;

/**
 * Created on 17/2/17.
 *
 * @author swayangjit
 */
public class MasterDataModel implements IReadable, ICleanable, IWritable {

    private Long id = -1L;

    // TODO: 19/4/17 Needs to be checked if the identifier will be hardcoded
    private String mIdentifier = "ekstep.domain.terms.list";

    private String mJson;
    private String mType;
    private AppContext mAppContext;

    private MasterDataModel(AppContext appContext, String type, String json) {
        mAppContext = appContext;
        mType = type;
        mJson = json;
    }

    public static MasterDataModel create(AppContext appContext, String type, String json) {
        return new MasterDataModel(appContext, type, json);
    }

    public static MasterDataModel findByType(AppContext appContext, String type) {
        MasterDataModel term = new MasterDataModel(appContext, type, null);
        appContext.getDBSession().read(term);
        return term;
    }

    public static MasterDataModel find(AppContext appContext) {
        MasterDataModel term = new MasterDataModel(appContext, null, null);
        appContext.getDBSession().read(term);
        return term;
    }

    private void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        mIdentifier = resultSet.getString(resultSet.getColumnIndex(MasterDataEntry.COLUMN_NAME_IDENTIFIER));
        mType = resultSet.getString(resultSet.getColumnIndex(MasterDataEntry.COLUMN_NAME_TYPE));
        mJson = resultSet.getString(resultSet.getColumnIndex(MasterDataEntry.COLUMN_NAME_JSON));
    }

    @Override
    public void clean() {
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", MasterDataEntry.COLUMN_NAME_TYPE, mType);
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            readWithoutMoving(resultSet);
        return this;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MasterDataEntry.COLUMN_NAME_IDENTIFIER, mIdentifier);
        contentValues.put(MasterDataEntry.COLUMN_NAME_TYPE, mType);
        contentValues.put(MasterDataEntry.COLUMN_NAME_JSON, mJson);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
    }

    @Override
    public String getTableName() {
        return MasterDataEntry.TABLE_NAME;
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
        String selectionCriteria = String.format(Locale.US, "where %s = '%s' AND %s = '%s'", MasterDataEntry.COLUMN_NAME_IDENTIFIER, mIdentifier, MasterDataEntry.COLUMN_NAME_TYPE, mType);
        String selectionCriteriaWithoutType = String.format(Locale.US, "where %s = '%s'", MasterDataEntry.COLUMN_NAME_IDENTIFIER, mIdentifier);
        return mType != null ? selectionCriteria : selectionCriteriaWithoutType;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[0];
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public void save() {
        mAppContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(AppContext context) {
                context.getDBSession().clean(MasterDataModel.this);
                context.getDBSession().create(MasterDataModel.this);
                return null;
            }
        });
    }

    public String getTermJson() {
        return mJson;
    }

    public String getTermType() {
        return mType;
    }

    public boolean exists() {
        return this.id != null && this.id != -1L;
    }
}
