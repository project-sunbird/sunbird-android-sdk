package org.ekstep.genieservices.async;

import org.ekstep.genieservices.commons.bean.GenieResponse;

public interface IPerformable<T> {
    public GenieResponse<T> perform();
}
