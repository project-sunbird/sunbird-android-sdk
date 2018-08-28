package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.List;
import java.util.Locale;

/**
 * Deleting game and language table.
 * <p>
 * Created on 4/20/2017.
 *
 * @author anil
 */
public class _08_PartnerFilterMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 8;
    private static final int TARGET_DB_VERSION = 13;

    public _08_PartnerFilterMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Add audience in content table and set the default as learner.
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForAudience());

        // Delete all entry from table.
        appContext.getDBSession().execute(String.format(Locale.US, "DELETE FROM %s", PageEntry.TABLE_NAME));

        // Add channel and medium in  page table .
        List<String> alterEntryForChannelAudience = PageEntry.getAlterEntryForChannelAndAudience();
        for (String alertEntry : alterEntryForChannelAudience) {
            appContext.getDBSession().execute(alertEntry);
        }
    }

}
