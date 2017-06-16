package org.ekstep.genieservices.commons.network;

import android.content.Context;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.network.auth.BasicAuthenticator;

/**
 * Created by mathew on 16/6/17.
 */

public class AndroidHttpClientFactory implements IHttpClientFactory {

    private AndroidAppContext mAppContext;

    public AndroidHttpClientFactory(AndroidAppContext appContext) {
        this.mAppContext = appContext;
    }

    @Override
    public IHttpClient getClient() {
        return new AndroidHttpClient(new BasicAuthenticator(mAppContext.getParams().getUserName(), mAppContext.getParams().getPassword()));
    }
}
