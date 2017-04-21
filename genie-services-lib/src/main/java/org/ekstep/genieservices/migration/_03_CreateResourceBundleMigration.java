package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.config.db.contract.ResourceBundleEntry;

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
