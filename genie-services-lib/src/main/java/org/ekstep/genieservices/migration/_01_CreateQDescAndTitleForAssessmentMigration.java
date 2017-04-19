package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.summarizer.db.contract.LearnerAssessmentsEntry;

import java.util.List;

public class _01_CreateQDescAndTitleForAssessmentMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 1;
    private static final int TARGET_DB_VERSION = 2;

    public _01_CreateQDescAndTitleForAssessmentMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        List<String> strings = LearnerAssessmentsEntry.getAlterEntryForQDescAndTitle();

        for (String query : strings) {
            appContext.getDBSession().execute(query);
        }
    }
}
