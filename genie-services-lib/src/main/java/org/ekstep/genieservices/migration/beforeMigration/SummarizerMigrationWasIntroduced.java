package org.ekstep.genieservices.migration.beforeMigration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.migration.BeforeMigrations;

import java.util.List;

public final class SummarizerMigrationWasIntroduced implements BeforeMigrations {

    // DO NOT TOUCH THIS CLASS. For any changes, please add a new migration.
    public void onCreate(AppContext appContext) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        List<String> createEntries = SummarizerDBContract.getTableCreateEntries();
        for (String entry : createEntries)
            appContext.getDBSession().execute(entry);
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    public void onUpgrade(AppContext appContext, int oldVersion, int newVersion) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        if (oldVersion == 1) {
            //do nothing
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

}
