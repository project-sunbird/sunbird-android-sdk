package org.ekstep.genieservices.commons.network;

import android.content.Context;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.network.auth.BasicAuthenticator;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public abstract class BaseAPI implements API {

    protected GenieResponse response;

    private String url;
    private OkHttpClient client;
    private AppContext<Context, AndroidLogger> appContext;
    private String TAG;

    public BaseAPI(String url, OkHttpClient client, AppContext<Context, AndroidLogger> appContext, String TAG) {
        this.url = url;
        this.client = client;
        this.appContext = appContext;
        this.TAG = TAG;

        response = GenieResponse.getSuccessResponse("", "", TAG);
    }

    @Override
    public void handleAuth() {
        client.setAuthenticator(new BasicAuthenticator());
    }

    @Override
    public Void fetchFromServer() {
        if (!appContext.getConnectionInfo().isConnected()) {
            response.setStatus(false);
            response.setError(NetworkConstants.CONNECTION_ERROR);
            response.setErrorMessages(Collections.singletonList(NetworkConstants.CONNECTION_ERROR_MESSAGE));

            return null;
        }

        try {
            configureClient();

            Request request = getRequest();

            Response response = client.newCall(request).execute();

            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                handle(responseBody);
            } else {
                this.response.setStatus(false);
                this.response.setError(NetworkConstants.SERVER_ERROR);
                this.response.setErrorMessages(Collections.singletonList(NetworkConstants.SERVER_ERROR_MESSAGE));
            }
        } catch (IOException e) {
            response.setStatus(false);
            response.setError(NetworkConstants.NETWORK_ERROR);
            response.setErrorMessages(Collections.singletonList(e.getMessage()));

            appContext.getLogger().error(TAG, e.getMessage());
        }

        return null;
    }

    private void configureClient() {
        client.setConnectTimeout(NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        client.setReadTimeout(NetworkConstants.NETWORK_READ_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    private Request getRequest() {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Accept-Encoding", "gzip, deflate")
                .url(url);

        decorateRequest(requestBuilder);

        return requestBuilder.build();
    }

    protected void decorateRequest(Request.Builder requestBuilder) {
        // majority request is post.
        String requestData = createRequestData();
        requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), requestData));
    }
}
