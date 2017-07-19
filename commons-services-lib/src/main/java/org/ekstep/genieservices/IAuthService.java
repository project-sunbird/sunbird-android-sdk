package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with all the required APIs to get security tokens for API management
 */
public interface IAuthService {

    /**
     * This api is used to get the bearer token required for invoking other APIs.
     * <p>
     * <p>
     * On successful generation of the token, the response will return the bearer token as result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE
     *
     * @return {@link GenieResponse<String>}
     */
    GenieResponse<String> getMobileDeviceBearerToken();

}
