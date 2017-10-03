package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.NoSqlEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Created by swayangjit on 10/9/17.
 */

public class _010_CreateKeyValueStoreMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 10;
    private static final int TARGET_DB_VERSION = 15;

    public _010_CreateKeyValueStoreMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(NoSqlEntry.getCreateEntry());
    }
}
