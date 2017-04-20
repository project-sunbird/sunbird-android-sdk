package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 18/4/17.
 */

public interface IDBTransaction {

    Void perform(AppContext context);

}
