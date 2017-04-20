package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GameEntry;
import org.ekstep.genieservices.commons.db.contract.LanguageEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.content.db.contract.ContentEntry;
import org.ekstep.genieservices.content.db.contract.PageEntry;
import org.ekstep.genieservices.partner.db.contract.PartnerEntry;
import org.ekstep.genieservices.profile.db.contract.ProfileEntry;
import org.ekstep.genieservices.profile.db.contract.UserEntry;
import org.ekstep.genieservices.telemetry.db.contract.EventPriority;
import org.ekstep.genieservices.telemetry.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryTagEntry;

import java.util.Arrays;
import java.util.List;

public class _01_CreatePageMigration extends Migration {

    private static final int MIGRATION_NUMBER = 1;
    private static final int TARGET_DB_VERSION = 7;

    public _01_CreatePageMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

//    @Override
//    public void apply(SQLiteDatabase db) {
//        db.execSQL(PageEntry.getCreateEntry());
//    }

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
                EventPriority.getCreateEntry(),
                UserEntry.getCreateEntry(),
                ProfileEntry.getCreateEntry(),
                ImportedMetadataEntry.getCreateEntry(),
                PartnerEntry.getCreateEntry(),
                LanguageEntry.getCreateEntry(),
                GameEntry.getCreateEntry(),
                ContentEntry.getCreateEntry(),
                TelemetryTagEntry.getCreateEntry()
        );
    }
}
