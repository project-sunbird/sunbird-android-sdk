package org.ekstep.genieservices.commons.db;

import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.List;

/**
 * @author anil
 */
public interface IDBContext {

    int getDBVersion();

    String getDBName();

    List<Migration> getMigrations();

}
