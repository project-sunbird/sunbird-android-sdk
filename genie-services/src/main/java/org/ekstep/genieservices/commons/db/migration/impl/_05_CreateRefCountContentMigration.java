package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.content.db.contract.ContentEntry;

public class _05_CreateRefCountContentMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 5;
    private static final int TARGET_DB_VERSION = 11;

    public _05_CreateRefCountContentMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForRefCount());
    }
}
