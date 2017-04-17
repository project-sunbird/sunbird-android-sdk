package org.ekstep.genieservices.commons.db.operations;

import org.ekstep.genieservices.commons.AppContext;


public interface IOperate {

    Void perform(AppContext appContext);

    void beforePerform(AppContext context);

}
