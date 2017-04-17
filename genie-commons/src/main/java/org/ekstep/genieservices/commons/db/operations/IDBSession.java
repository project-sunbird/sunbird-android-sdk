package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.core.ICleanDb;
import org.ekstep.genieservices.commons.db.core.IReadDb;
import org.ekstep.genieservices.commons.db.core.IUpdateDb;
import org.ekstep.genieservices.commons.db.core.IWriteToDb;

import java.util.List;

/**
 * Created on 4/17/2017.
 *
 * @author anil
 */
public interface IDBSession {

    Void execute(IOperate operate);

    Void executeInOneTransaction(List<IOperate> dbOperators);

    ServiceDbHelper getDbHelper();

    IOperate getCleaner(ICleanDb cleanDb);

    IOperate getReader(IReadDb readDb);

    IOperate getWriter(IWriteToDb writeToDb);

    IOperate getUpdater(IUpdateDb updateDb);

    IOperate getQueryExecutor(String query);
}
