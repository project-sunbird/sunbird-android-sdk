package org.ekstep.genieservices.commons.db;


import org.ekstep.genieservices.commons.db.migration.BeforeMigrations;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.migration.Migrations;
import org.ekstep.genieservices.migration.beforeMigration.BeforeMigrationWasIntroduced;

import java.util.List;

public class GSDBContext implements IDBContext {
    // Please don't make any changes in the class, except DATABASE_VERSION value.
    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "GenieServices.db";
    private List<Migration> migrations;

    public GSDBContext() {
        setMigrations(Migrations.getGeServiceMigrations());
    }

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
        return migrations;
    }

    public void setMigrations(List<Migration> migrations) {
        this.migrations = migrations;
    }

    @Override
    public BeforeMigrations getMigrationIntroduced() {
        return new BeforeMigrationWasIntroduced();
    }
}
