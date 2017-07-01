package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.network.auth.BasicAuthenticator;

/**
 * Created on 16/6/17.
 *
 * @author mathew
 */
public class AndroidHttpClientFactory implements IHttpClientFactory {

    private AndroidAppContext mAppContext;

    public AndroidHttpClientFactory(AndroidAppContext appContext) {
        this.mAppContext = appContext;
    }

    @Override
    public IHttpClient getClient() {
        return new AndroidHttpClient(new BasicAuthenticator(mAppContext.getParams().getString(ServiceConstants.Params.API_USER), mAppContext.getParams().getString(ServiceConstants.Params.API_PASS)));
    }
}
