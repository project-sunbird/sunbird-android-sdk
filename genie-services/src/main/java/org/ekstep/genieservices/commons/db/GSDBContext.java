package org.ekstep.genieservices.commons.db;


public class GSDBContext implements DBContext {
    // Please don't make any changes in the class, except DATABASE_VERSION value.
    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "GenieServices.db";
//    private List<Migration> migrations;

    public GSDBContext() {
//        setMigrations(Migrations.getGeServiceMigrations());
    }

    @Override
    public int getDBVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public String getDBName() {
        return DATABASE_NAME;
    }

//    @Override
//    public List<Migration> getMigrations() {
//        return migrations;
//    }

//    public void setMigrations(List<Migration> migrations) {
//        this.migrations = migrations;
//    }

//    @Override
//    public BeforeMigrations getMigrationIntroduced() {
//        return new BeforeMigrationWasIntroduced();
//    }
}
