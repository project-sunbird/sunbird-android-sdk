package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.EventPriorityEntry;
import org.ekstep.genieservices.commons.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.contract.PartnerEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;
import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;
import org.ekstep.genieservices.commons.db.contract.UserEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.Arrays;
import java.util.List;

public class _01_CreatePageMigration extends Migration {

    private static final int MIGRATION_NUMBER = 1;
    private static final int TARGET_DB_VERSION = 7;

    public _01_CreatePageMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // COPIED FROM BEFORE MIGRATION
        List<String> createEntries = getSqlCreateEntries();
        for (String entry : createEntries) {
            appContext.getDBSession().execute(entry);
        }

        // Add column day and month to profile
        List<String> alterEntryDayAndMonthToProfile = ProfileEntry.getAlterEntryDayAndMonthToProfile();
        for (String alertEntry : alterEntryDayAndMonthToProfile) {
            appContext.getDBSession().execute(alertEntry);
        }

        appContext.getDBSession().execute(ImportedMetadataEntry.getUpdateEntryForCountColumn());
        appContext.getDBSession().execute(ProfileEntry.getAlterEntryIsGroupUser());
        appContext.getDBSession().execute(TelemetryTagEntry.getCreateEntry());
        // COPIED FROM BEFORE MIGRATION

        appContext.getDBSession().execute(PageEntry.getCreateEntry());
    }


    private List<String> getSqlCreateEntries() {
        return Arrays.asList(
                TelemetryEntry.getCreateEntry(),
                TelemetryProcessedEntry.getCreateEntry(),
                EventPriorityEntry.getCreateEntry(),
                UserEntry.getCreateEntry(),
                ProfileEntry.getCreateEntry(),
                ImportedMetadataEntry.getCreateEntry(),
                PartnerEntry.getCreateEntry(),
                ContentEntry.getCreateEntry(),
                TelemetryTagEntry.getCreateEntry()
        );
    }
}
