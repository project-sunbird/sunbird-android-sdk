package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentMarkerEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Created on 22/11/18.
 * anil
 */
public class _15_ContentMarkerMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 15;
    private static final int TARGET_DB_VERSION = 20;

    public _15_ContentMarkerMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(ContentMarkerEntry.getCreateEntry());
    }

}
