package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.operations.IDBOperation;

import java.util.Locale;

public class SQLiteCleaner implements IDBOperation<SQLiteDatabase> {

    private ICleanDb model;

    public SQLiteCleaner(ICleanDb model) {
        this.model = model;
    }

    @Override
    public Void perform(AppContext context, SQLiteDatabase datasource) {
        String query = String.format(Locale.US, "DELETE FROM %s %s", model.getTableName(), model.selectionToClean());
        datasource.execSQL(query);
        model.clean();
        return null;
    }

}
