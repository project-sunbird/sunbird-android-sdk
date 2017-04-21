package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.content.db.contract.ContentFeedbackEntry;

public class _06_CreateContentFeedbackMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 6;
    private static final int TARGET_DB_VERSION = 11;

    public _06_CreateContentFeedbackMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(ContentFeedbackEntry.getCreateEntry());
    }
}
