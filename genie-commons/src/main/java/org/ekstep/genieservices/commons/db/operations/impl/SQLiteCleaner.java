package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.sqlite.SQLiteDatabase;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.operations.IOperate;

import java.util.Locale;

public class SQLiteCleaner implements IOperate<SQLiteDatabase> {

    private ICleanDb model;

    public SQLiteCleaner(ICleanDb model) {
        this.model = model;
    }

    @Override
    public Void perform(SQLiteDatabase datasource) {
        String query = String.format(Locale.US, "DELETE FROM %s %s", model.getTableName(), model.selectionToClean());
        datasource.execSQL(query);
        model.clean();
        return null;
    }

    @Override
    public void beforePerform(AppContext context) {

    }
}
