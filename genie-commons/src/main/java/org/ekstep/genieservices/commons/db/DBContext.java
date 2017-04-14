package org.ekstep.genieservices.commons.db;


public interface DBContext {

    int getDBVersion();

    String getDBName();

//    List<Migration> getMigrations();
//
//    BeforeMigrations getMigrationIntroduced();

}
