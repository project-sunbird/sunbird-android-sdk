package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.IUpdateDb;
import org.ekstep.genieservices.commons.db.core.IWriteToDb;

/**
 * Created on 4/17/2017.
 *
 * @author anil
 */
public interface IDBSession {

    Void beginTransaction();

    Void endTransaction();

    Void clean(ICleanDb cleanDb);

    Void read(IReadDb readDb);

    Void create(IWriteToDb writeToDb);

    Void update(IUpdateDb updateDb);

    Void execute(String query);

    Void executeInTransaction(IDBOperate operate);
}
