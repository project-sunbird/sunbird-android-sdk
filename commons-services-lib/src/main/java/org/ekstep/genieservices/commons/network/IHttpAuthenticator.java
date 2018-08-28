package org.ekstep.genieservices.commons.network;

import java.util.Map;

/**
 * Created on 6/7/17.
 */

public interface IHttpAuthenticator {

    public Map<String, String> getAuthHeaders();

}
