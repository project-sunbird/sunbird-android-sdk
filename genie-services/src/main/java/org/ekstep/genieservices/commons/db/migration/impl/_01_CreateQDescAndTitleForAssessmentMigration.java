package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.summarizer.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.summarizer.db.contract.LearnerContentSummaryEntry;

import java.util.List;

public class _01_CreateQDescAndTitleForAssessmentMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 1;
    private static final int TARGET_DB_VERSION = 2;

    public _01_CreateQDescAndTitleForAssessmentMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // COPIED FROM BEFORE MIGRATION
        appContext.getDBSession().execute(LearnerAssessmentsEntry.getCreateEntry());
        appContext.getDBSession().execute(LearnerContentSummaryEntry.getCreateEntry());

        List<String> strings = LearnerAssessmentsEntry.getAlterEntryForQDescAndTitle();
        for (String query : strings) {
            appContext.getDBSession().execute(query);
        }
    }

}
