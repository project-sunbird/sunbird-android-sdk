package org.ekstep.genieservices.commons.network;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.utils.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public abstract class BaseAPI {

    private static final String GET = "GET";
    private static final String POST = "POST";

    private AppContext appContext;
    private IHttpClient httpClient;
    private Map<String, String> headers;
    private String url;
    private String TAG;

    public BaseAPI(AppContext appContext, String url, String TAG) {
        this.url = url;
        this.appContext = appContext;
        this.TAG = TAG;
        this.httpClient = appContext.getHttpClient();
        this.headers = new HashMap<>();
        addHttpHeaders("Accept-Encoding", "gzip, deflate");
    }

    public void handleAuth() {
        httpClient.handleAuth();
    }

    public void addHttpHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    public GenieResponse get() {
        return fetchFromServer(GET);
    }

    public GenieResponse post() {
        return fetchFromServer(POST);
    }

    private GenieResponse fetchFromServer(String requestType) {
        if (!appContext.getConnectionInfo().isConnected()) {
            return GenieResponse.getErrorResponse(appContext, NetworkConstants.CONNECTION_ERROR, NetworkConstants.CONNECTION_ERROR_MESSAGE, TAG);
        }
        try {
            httpClient.createClient();
            httpClient.setTimeouts(NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES, NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES);
            handleAuth();
            httpClient.createRequest(url);
            httpClient.setHeaders(headers);
            httpClient.setHeaders(getRequestHeaders());
            ApiResponse response = null;
            if (GET.equals(requestType)) {
                response = httpClient.doGet();
            } else if (POST.equals(requestType)) {
                response = httpClient.doPost(createRequestData());
            }
            if (response.isSuccessful()) {
                handle(parseResult(response.getResponseBody()));
            } else {
                return GenieResponse.getErrorResponse(appContext, NetworkConstants.SERVER_ERROR, NetworkConstants.SERVER_ERROR_MESSAGE, TAG);
            }
        } catch (IOException e) {
            Logger.e(appContext, TAG, e.getMessage());
            return GenieResponse.getErrorResponse(appContext, NetworkConstants.NETWORK_ERROR, e.getMessage(), TAG);
        }
        return null;
    }

    protected abstract Map<String,String> getRequestHeaders();
    protected abstract String createRequestData();
    protected abstract void handle(LinkedTreeMap result) throws IOException;

    protected LinkedTreeMap parseResult(String body) {
        LinkedTreeMap map = new Gson().fromJson(body, LinkedTreeMap.class);
        return (LinkedTreeMap) map.get("result");
    }

}
