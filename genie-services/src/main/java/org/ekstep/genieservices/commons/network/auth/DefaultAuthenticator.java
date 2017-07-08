package org.ekstep.genieservices.commons.network.auth;

import com.squareup.okhttp.Credentials;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.IHttpAuthenticator;
import org.ekstep.genieservices.commons.network.NetworkConstants;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public class DefaultAuthenticator implements IHttpAuthenticator {

    private AppContext mAppContext;

    public DefaultAuthenticator(AppContext appContext) {
        this.mAppContext = appContext;
    }

    @Override
    public Map<String, String> getAuthHeaders() {
        String mobileAppKey = mAppContext.getParams().getString(ServiceConstants.Params.MOBILE_APP_KEY);
        if (!StringUtil.isNullOrEmpty(mobileAppKey)) {
            return getApiGatewayHeaders();
        } else {
            return getBasicAuthHeaders();
        }
    }

    private Map<String, String> getBasicAuthHeaders() {
        Map<String, String> authHeaders = new HashMap<>();
        String user = mAppContext.getParams().getString(ServiceConstants.Params.API_USER);
        String pass = mAppContext.getParams().getString(ServiceConstants.Params.API_PASS);
        String credentials = Credentials.basic(user, pass);
        authHeaders.put("Authorization", credentials);
        return authHeaders;
    }

    private Map<String, String> getApiGatewayHeaders() {
        Map<String, String> authHeaders = new HashMap<>();
        String bearerToken = mAppContext.getKeyValueStore().getString(NetworkConstants.API_BEARER_TOKEN, "");
        authHeaders.put("Authorization", "Bearer " + bearerToken);
        return authHeaders;
    }

}
