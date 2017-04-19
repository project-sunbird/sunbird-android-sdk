package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.AppContext;

public interface IDBOperation<T> {

    Void perform(AppContext appContext, T datasouce);

}
