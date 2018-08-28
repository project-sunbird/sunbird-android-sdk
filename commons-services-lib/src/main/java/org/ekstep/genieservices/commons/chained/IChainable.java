package org.ekstep.genieservices.commons.chained;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public interface IChainable<T, K> {
    GenieResponse<T> execute(AppContext appContext, K context);

    IChainable<T, K> then(IChainable<T, K> link);
}
