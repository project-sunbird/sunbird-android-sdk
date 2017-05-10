package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.GameEntry;
import org.ekstep.genieservices.commons.db.contract.LanguageEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;

/**
 * Deleting game and language table.
 * <p>
 * Created on 4/20/2017.
 *
 * @author anil
 */
public class _08_DeleteGameAndLanguageEntryMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 8;
    private static final int TARGET_DB_VERSION = 13;

    public _08_DeleteGameAndLanguageEntryMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        appContext.getDBSession().execute(GameEntry.getDeleteEntry());
        appContext.getDBSession().execute(LanguageEntry.getDeleteEntry());
    }

}
