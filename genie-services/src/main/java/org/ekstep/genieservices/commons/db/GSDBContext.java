package org.ekstep.genieservices.commons.db;

import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.db.migration.impl.Migrations;

import java.util.List;

/**
 * @author anil
 */
public class GSDBContext implements IDBContext {

    // Please don't make any changes in the class, except DATABASE_VERSION value.
    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "GenieServices.db";

    @Override
    public int getDBVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public String getDBName() {
        return DATABASE_NAME;
    }

    @Override
    public List<Migration> getMigrations() {
        return Migrations.getGeServiceMigrations();
    }

}
