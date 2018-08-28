package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;

/**
 * Created on 4/17/2017.
 *
 * @author anil
 */
public interface IDBSession {

    String getDBName();

    int getDBVersion();

    String getDatabasePath();

    Void beginTransaction();

    Void endTransaction();

    Void clean(ICleanable cleanDb);

    Void read(IReadable readDb);

    Void read(IReadable readDb, String customQuery);

    Void create(IWritable writeToDb);

    Void update(IUpdatable updateDb);

    Void execute(String query);

    Void executeInTransaction(IDBTransaction transaction);

}
