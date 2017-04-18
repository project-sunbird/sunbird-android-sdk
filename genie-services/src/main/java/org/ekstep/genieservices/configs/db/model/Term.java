package org.ekstep.genieservices.configs.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.IWriteToDb;
import org.ekstep.genieservices.commons.db.core.ResultSet;
import org.ekstep.genieservices.commons.db.core.impl.ContentValues;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.configs.db.contract.TermEntry;

import java.util.Locale;

/**
 * Created on 17/2/17.
 *
 * @author swayangjit
 */
public class Term implements IReadDb, ICleanDb, IWriteToDb {

    private Long id = -1L;
    private String mIdentifier = "ekstep.domain.terms.list";

    private String mTermJson;
    private String mTermType;
    private AppContext mAppContext;

    private Term(AppContext appContext, String type, String json) {
        mAppContext = appContext;
        mTermType = type;
        mTermJson = json;
    }

    public static Term build(AppContext appContext, String type, String json) {
        return new Term(appContext, type, json);
    }

    public static Term find(AppContext appContext, String type) {
        Term term = new Term(appContext, type, null);
        appContext.getDBSession().read(term);
        return term;
    }

    private void readWithoutMoving(ResultSet resultSet) {
        id = resultSet.getLong(0);
        mIdentifier = resultSet.getString(resultSet.getColumnIndex(TermEntry.COLUMN_NAME_IDENTIFIER));
        mTermType = resultSet.getString(resultSet.getColumnIndex(TermEntry.COLUMN_NAME_TERM_TYPE));
        mTermJson = resultSet.getString(resultSet.getColumnIndex(TermEntry.COLUMN_NAME_TERM_JSON));
    }

    @Override
    public void clean() {
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", TermEntry.COLUMN_NAME_TERM_TYPE, mTermType);
    }

    @Override
    public IReadDb read(ResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            readWithoutMoving(resultSet);
        return this;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TermEntry.COLUMN_NAME_IDENTIFIER, mIdentifier);
        contentValues.put(TermEntry.COLUMN_NAME_TERM_TYPE, mTermType);
        contentValues.put(TermEntry.COLUMN_NAME_TERM_JSON, mTermJson);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
    }

    @Override
    public String getTableName() {
        return TermEntry.TABLE_NAME;
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
        String selectionCriteria = String.format(Locale.US, "where %s = '%s' AND %s = '%s'", TermEntry.COLUMN_NAME_IDENTIFIER, mIdentifier, TermEntry.COLUMN_NAME_TERM_TYPE, mTermType);
        String selectionCriteriaWithoutType = String.format(Locale.US, "where %s = '%s'", TermEntry.COLUMN_NAME_IDENTIFIER, mIdentifier);
        return mTermType != null ? selectionCriteria : selectionCriteriaWithoutType;
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
                context.getDBSession().clean(Term.this);
                context.getDBSession().create(Term.this);
                return null;
            }
        });
    }

    public String getTermJson() {
        return mTermJson;
    }

    public String getTermType() {
        return mTermType;
    }

    public boolean exists() {
        return this.id != null && this.id != -1L;
    }
}
