package org.ekstep.genieservices.commons.db;


import org.ekstep.genieservices.commons.db.migration.BeforeMigrations;
import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.List;

public interface IDBContext {

    int getDBVersion();

    String getDBName();

    List<Migration> getMigrations();

    BeforeMigrations getMigrationIntroduced();

}
