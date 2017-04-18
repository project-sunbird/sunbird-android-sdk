package org.ekstep.genieservices.commons.db;

public class SummarizerDBContext implements DBContext {
    // Please don't make any changes in the class, except DATABASE_VERSION value.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Summarizer.db";

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
//        return SummarizerMigrations.getSummarizerMigrations();
//    }
//
//    @Override
//    public BeforeMigrations getMigrationIntroduced() {
//        return new SummarizerMigrationWasIntroduced();
//    }
}
