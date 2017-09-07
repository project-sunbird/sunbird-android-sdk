package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.network.auth.DefaultAuthenticator;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created on 16/6/17.
 */
public class AndroidHttpClientFactory implements IHttpClientFactory {

    private AndroidAppContext mAppContext;
    private OkHttpClient httpClient;

    public AndroidHttpClientFactory(AndroidAppContext appContext) {
        this.mAppContext = appContext;

        int readTimeout = appContext.getParams().getInt(IParams.Key.NETWORK_READ_TIMEOUT);
        int connectTimeout = appContext.getParams().getInt(IParams.Key.NETWORK_CONNECT_TIMEOUT);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        httpClient = builder.build();
    }

    @Override
    public IHttpClient getClient() {
        return new AndroidHttpClient(httpClient);
    }

    @Override
    public IHttpAuthenticator getHttpAuthenticator() {
        return new DefaultAuthenticator(mAppContext);
    }
}
