package org.ekstep.genieservices.commons.chained;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public interface IChainable<T> {
    GenieResponse<T> execute(AppContext appContext, ImportContext importContext);

    IChainable<T> then(IChainable<T> link);
}
