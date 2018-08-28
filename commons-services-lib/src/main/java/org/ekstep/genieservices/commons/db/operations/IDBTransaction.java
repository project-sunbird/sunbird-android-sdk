package org.ekstep.genieservices.commons.db.operations;

/**
 * Created on 18/4/17.
 */

public interface IDBTransaction {

    Void perform(IDBSession dbSession);

}
