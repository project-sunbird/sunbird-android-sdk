package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Created on 29/5/18.
 * shriharsh
 */
public class _12_ProfileSyllabusMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 12;
    private static final int TARGET_DB_VERSION = 16;

    public _12_ProfileSyllabusMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Add syllabus in Profile table.
        appContext.getDBSession().execute(ProfileEntry.getAlterEntryForProfileSyllabus());
    }

}
