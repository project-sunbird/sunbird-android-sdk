package org.ekstep.genieservices.configs.db.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import org.ekstep.genieservices.commons.db.DbOperator;
import org.ekstep.genieservices.commons.db.operations.impl.Cleaner;
import org.ekstep.genieservices.commons.db.operations.IOperate;
import org.ekstep.genieservices.commons.db.operations.impl.Reader;
import org.ekstep.genieservices.commons.db.operations.impl.Writer;
import org.ekstep.genieservices.commons.db.operations.ICleanDb;
import org.ekstep.genieservices.commons.db.operations.IReadDb;
import org.ekstep.genieservices.commons.db.operations.IWriteToDb;
import org.ekstep.genieservices.configs.db.contract.TermEntry;

import java.util.ArrayList;
import java.util.List;
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

    public Term(String type, String json) {
        this.mTermType = type;
        this.mTermJson = json;
    }

    public Term(String type) {
        this.mTermType = type;
    }

    public Term() {
    }

    public void readWithoutMoving(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        mIdentifier = cursor.getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_IDENTIFIER));
        mTermType = cursor.getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_TERM_TYPE));
        mTermJson = cursor.getString(cursor.getColumnIndex(TermEntry.COLUMN_NAME_TERM_JSON));
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", TermEntry.COLUMN_NAME_TERM_TYPE, mTermType);
    }

    @Override
    public IReadDb read(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst())
            readWithoutMoving(cursor);
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
    public void beforeWrite(Context context) {

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

    public void save(DbOperator dbOperator) {
        List<IOperate> tasks = new ArrayList<>();
        tasks.add(new Cleaner(new Term(mTermType)));
        tasks.add(new Writer(this));
        dbOperator.executeInOneTransaction(tasks);
    }


    public String getTermJson() {
        return mTermJson;
    }

    public String getTermType() {
        return mTermType;
    }

    public boolean exists(DbOperator dbOperator) {
        Term term = new Term();
        Reader otherTermReader = new Reader(term);
        dbOperator.execute(otherTermReader);

        return term.id != null && term.id != -1L;
    }
}
