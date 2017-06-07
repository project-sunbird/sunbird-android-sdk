package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerContentSummaryEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

public class _10_SummarizerMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 10;
    private static final int TARGET_DB_VERSION = 15;

    public _10_SummarizerMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // COPIED FROM BEFORE MIGRATION
        appContext.getDBSession().execute(LearnerAssessmentsEntry.getCreateEntry());
        appContext.getDBSession().execute(LearnerContentSummaryEntry.getCreateEntry());
    }

}
