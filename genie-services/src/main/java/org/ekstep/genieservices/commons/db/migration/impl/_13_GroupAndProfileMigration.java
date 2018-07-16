package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GroupEntry;
import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Created on 16/7/18.
 * shriharsh
 */
public class _13_GroupAndProfileMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 13;
    private static final int TARGET_DB_VERSION = 18;


    public _13_GroupAndProfileMigration(int migrationNumber, int targetDbVersion) {
        super(migrationNumber, targetDbVersion);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(GroupEntry.getCreateEntry());
        appContext.getDBSession().execute(GroupProfileEntry.getCreateEntry());
    }
}
