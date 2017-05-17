package org.ekstep.genieservices.content.chained;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.content.bean.ImportContext;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public interface IChainable {
    GenieResponse<Void> execute(AppContext appContext, ImportContext importContext);

    Void postExecute();

    GenieResponse<Void> breakChain();

    IChainable then(IChainable link);
}
