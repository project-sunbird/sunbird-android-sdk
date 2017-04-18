package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.AppContext;


public interface IOperate<T> {

    Void perform(T datasouce);

    void beforePerform(AppContext context);

}
