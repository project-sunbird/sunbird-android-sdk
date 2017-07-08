package org.ekstep.genieservices.commons.network;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 20/4/17.
 */

public class AndroidHttpClient implements IHttpClient {

    private OkHttpClient mHttpClient;
    private Request.Builder requestBuilder;

    private IHttpAuthenticator authenticator;

    public AndroidHttpClient(IHttpAuthenticator authenticator) {
        this.authenticator = authenticator;
        this.mHttpClient = new OkHttpClient();
    }

    @Override
    public Void createRequest(String url) {
        this.requestBuilder = new Request.Builder().url(url);
        return null;
    }

    @Override
    public Void setHeaders(Map<String, String> headers) {
        if (headers != null) {
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> keyValuePair = (Map.Entry) it.next();
                this.requestBuilder.addHeader(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
        return null;
    }

    @Override
    public ApiResponse doGet() throws IOException {
        requestBuilder.get();
        Request request = requestBuilder.build();
        Response response = mHttpClient.newCall(request).execute();
        return new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
    }

    @Override
    public ApiResponse doPost(byte[] requestBody) throws IOException {
        requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), requestBody));
        Request request = requestBuilder.build();
        Response response = mHttpClient.newCall(request).execute();
        return new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
    }

    @Override
    public Void setTimeouts(int connectionTimeout, int readTimeout) {
        mHttpClient.setConnectTimeout(NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        mHttpClient.setReadTimeout(NetworkConstants.NETWORK_READ_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public Void setAuthHeaders() {
        setHeaders(authenticator.getAuthHeaders());
        return null;
    }

}
