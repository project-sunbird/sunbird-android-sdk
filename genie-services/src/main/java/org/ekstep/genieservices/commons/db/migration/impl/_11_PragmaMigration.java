package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Adding pragma column in content table
 * <p>
 * Created on 1/31/2018.
 *
 * @author anil
 */
public class _11_PragmaMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 11;
    private static final int TARGET_DB_VERSION = 16;

    public _11_PragmaMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Add pragma in content table.
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForPragma());

        // TODO: 31/1/18 - Scan local content column and make entry in pragma column.

        // Add pragma in page table.
        appContext.getDBSession().execute(PageEntry.getAlterEntryForPragma());
    }

}
