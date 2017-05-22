package org.ekstep.genieservices.async;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created by mathew on 22/5/17.
 */

public interface IPerformable<T> {
    public GenieResponse<T> perform();
}
