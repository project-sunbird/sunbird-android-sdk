package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.content.db.contract.PageEntry;

public class _01_CreatePageMigration extends Migration {

    private static final int MIGRATION_NUMBER = 1;
    private static final int TARGET_DB_VERSION = 7;

    public _01_CreatePageMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

//    @Override
//    public void apply(SQLiteDatabase db) {
//        db.execSQL(PageEntry.getCreateEntry());
//    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(PageEntry.getCreateEntry());
    }
}
