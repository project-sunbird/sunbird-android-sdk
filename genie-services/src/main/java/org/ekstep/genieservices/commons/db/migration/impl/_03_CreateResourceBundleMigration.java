package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ResourceBundleEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

public class _03_CreateResourceBundleMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 3;
    private static final int TARGET_DB_VERSION = 10;

    public _03_CreateResourceBundleMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(ResourceBundleEntry.getCreateEntry());
    }
}
