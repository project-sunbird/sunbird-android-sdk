package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.OrdinalsEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

public class _04_CreateOrdinalsMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 4;
    private static final int TARGET_DB_VERSION = 10;

    public _04_CreateOrdinalsMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(OrdinalsEntry.getCreateEntry());
    }
}
