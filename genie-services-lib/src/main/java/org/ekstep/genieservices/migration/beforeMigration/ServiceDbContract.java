package org.ekstep.genieservices.migration.beforeMigration;

import org.ekstep.genieservices.commons.db.contract.GameEntry;
import org.ekstep.genieservices.commons.db.contract.LanguageEntry;
import org.ekstep.genieservices.content.db.contract.ContentEntry;
import org.ekstep.genieservices.partner.db.contract.PartnerEntry;
import org.ekstep.genieservices.profile.db.contract.ProfileEntry;
import org.ekstep.genieservices.profile.db.contract.UserEntry;
import org.ekstep.genieservices.telemetry.db.contract.EventPriority;
import org.ekstep.genieservices.telemetry.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryTagEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ServiceDbContract {

    public static List<String> getSqlCreateEntries() {
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

    public static List<String> getSqlDeleteEntries() {
        return Arrays.asList(
                TelemetryEntry.getDeleteEntry(),
                TelemetryProcessedEntry.getDeleteEntry(),
                EventPriority.getDeleteEntry(),
                UserEntry.getDeleteEntry(),
                ProfileEntry.getDeleteEntry(),
                ImportedMetadataEntry.getDeleteEntry(),
                PartnerEntry.getDeleteEntry(),
                LanguageEntry.getDeleteEntry(),
                GameEntry.getDeleteEntry(),
                ContentEntry.getDeleteEntry(),
                TelemetryTagEntry.getDeleteEntry()
        );
    }

    public static List<String> getSqlBootstrapData() {
        ArrayList<String> queries = new ArrayList<>();
        queries.addAll(GameEntry.getBootstrapData());
        queries.add(LanguageEntry.getBootstrapEntry());
        return queries;
    }
}


