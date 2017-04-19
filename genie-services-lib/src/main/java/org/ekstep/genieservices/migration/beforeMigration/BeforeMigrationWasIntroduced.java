package org.ekstep.genieservices.migration.beforeMigration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GameEntry;
import org.ekstep.genieservices.commons.db.migration.BeforeMigrations;
import org.ekstep.genieservices.content.db.contract.ContentEntry;
import org.ekstep.genieservices.profile.db.contract.ProfileEntry;
import org.ekstep.genieservices.telemetry.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryTagEntry;

import java.util.List;

public final class BeforeMigrationWasIntroduced implements BeforeMigrations {

    private static void updateSearchIndexForExistingContent(AppContext appContext) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        List<String> updateEntries = ContentEntry.getUpdateEntryWithSearchIndexColumn();
        for (String updateEntry : updateEntries) {
            try {
                appContext.getDBSession().execute(updateEntry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // TODO: 4/19/2017 Anil - Uncomment after model is ready for Contents
//        Contents contents = new Contents("");
//        Reader reader = new Reader(contents);
//        reader.perform(db);
//
//        List<IOperate> migrationsFromV1_0ToV1_1 = contents.getMigrationsFromV1_0ToV1_1();
//        for (IOperate migration : migrationsFromV1_0ToV1_1) {
//            migration.perform(db);
//        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    private static void addColumnDayAndMonthToProfile(AppContext appContext) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        List<String> alterEntryDayAndMonthToProfile = ProfileEntry.getAlterEntryDayAndMonthToProfile();
        for (String alertEntry : alterEntryDayAndMonthToProfile) {
            appContext.getDBSession().execute(alertEntry);
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    // DO NOT TOUCH THIS CLASS. For any changes, please add a new migration.
    @Override
    public void onCreate(AppContext appContext) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        List<String> createEntries = ServiceDbContract.getSqlCreateEntries();
        for (String entry : createEntries) {
            appContext.getDBSession().execute(entry);
        }

        List<String> bootstrapData = ServiceDbContract.getSqlBootstrapData();
        for (String query : bootstrapData) {
            appContext.getDBSession().execute(query);
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

    @Override
    public void onUpgrade(AppContext appContext, int oldVersion, int newVersion) {
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
        if (oldVersion == 1) {
            String deleteQuery = GameEntry.getDeleteEntry();
            appContext.getDBSession().execute(deleteQuery);

            String createQuery = GameEntry.getCreateEntry();
            appContext.getDBSession().execute(createQuery);

            List<String> gameBootstrapQueries = GameEntry.getBootstrapData();
            for (String query : gameBootstrapQueries) {
                appContext.getDBSession().execute(query);
            }
        }

        String createContent = ContentEntry.getCreateEntry();
        appContext.getDBSession().execute(createContent);

        if (oldVersion <= 4) {   // oldVersion == 4 || oldVersion == 3
            updateSearchIndexForExistingContent(appContext);
        }

        if (oldVersion <= 6) {
            addColumnDayAndMonthToProfile(appContext);
            appContext.getDBSession().execute(ImportedMetadataEntry.getUpdateEntryForCountColumn());
            appContext.getDBSession().execute(ProfileEntry.getAlterEntryIsGroupUser());
            appContext.getDBSession().execute(TelemetryTagEntry.getCreateEntry());
        }
        // DO NOT TOUCH THIS METHOD. For any changes, please add a new migration.
    }

}
