package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.network.auth.DefaultAuthenticator;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.R.attr.key;

/**
 * Created on 16/6/17.
 */
public class AndroidHttpClientFactory implements IHttpClientFactory {

    private AndroidAppContext mAppContext;
    private OkHttpClient httpClient;

    public AndroidHttpClientFactory(AndroidAppContext appContext) {
        this.mAppContext = appContext;
        String connectTimeout = appContext.getParams().getString(ServiceConstants.Params.NETWORK_CONNECT_TIMEOUT);
        String readTimeout = appContext.getParams().getString(ServiceConstants.Params.NETWORK_READ_TIMEOUT);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(StringUtil.isNullOrEmpty(readTimeout) ? NetworkConstants.NETWORK_READ_TIMEOUT : Integer.valueOf(readTimeout), TimeUnit.SECONDS);
        builder.connectTimeout(StringUtil.isNullOrEmpty(connectTimeout) ? NetworkConstants.NETWORK_CONNECT_TIMEOUT : Integer.valueOf(connectTimeout), TimeUnit.SECONDS);
        httpClient = builder.build();
    }

    @Override
    public IHttpClient getClient() {
        return new AndroidHttpClient(httpClient, new DefaultAuthenticator(mAppContext));
    }
}
