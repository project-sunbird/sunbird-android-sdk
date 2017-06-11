package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GameEntry;
import org.ekstep.genieservices.commons.db.contract.LanguageEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerContentSummaryEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

public class _09_SdkMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 9;
    private static final int TARGET_DB_VERSION = 14;

    public _09_SdkMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Delete Game Entry
        appContext.getDBSession().execute(GameEntry.getDeleteEntry());
        // Delete Language Entry
        appContext.getDBSession().execute(LanguageEntry.getDeleteEntry());

        // Summarizer related tables
        appContext.getDBSession().execute(LearnerAssessmentsEntry.getCreateEntry());
        appContext.getDBSession().execute(LearnerContentSummaryEntry.getCreateEntry());
    }

}