package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
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
    private IHttpClientFactory httpClientFactory;
    private Map<String, String> headers;
    private String url;
    private String TAG;

    public BaseAPI(AppContext appContext, String url, String TAG) {
        this.url = url;
        this.appContext = appContext;
        this.TAG = TAG;
        this.httpClientFactory = appContext.getHttpClientFactory();
        this.headers = new HashMap<>();
        this.headers.put("Accept-Encoding", "gzip, deflate");
    }

    public GenieResponse get() {
        return fetchFromServer(GET);
    }

    public GenieResponse post() {
        return fetchFromServer(POST);
    }

    private GenieResponse fetchFromServer(String requestType) {
        if (!appContext.getConnectionInfo().isConnected()) {
            return GenieResponseBuilder.getErrorResponse(NetworkConstants.CONNECTION_ERROR, NetworkConstants.CONNECTION_ERROR_MESSAGE, TAG, String.class);
        }
        try {
            IHttpClient httpClient = httpClientFactory.getClient();
            httpClient.setTimeouts(NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES, NetworkConstants.NETWORK_CONNECT_TIMEOUT_MINUTES);
            httpClient.handleAuth();
            httpClient.createRequest(url);
            httpClient.setHeaders(headers);
            httpClient.setHeaders(getRequestHeaders());
            ApiResponse apiResponse = null;
            if (GET.equals(requestType)) {
                apiResponse = httpClient.doGet();
            } else if (POST.equals(requestType)) {
                apiResponse = httpClient.doPost(getRequestData());
            }
            if (apiResponse.isSuccessful()) {
                GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse("", String.class);
                response.setResult(apiResponse.getResponseBody());
                return response;
            } else {
                return GenieResponseBuilder.getErrorResponse(NetworkConstants.SERVER_ERROR, NetworkConstants.SERVER_ERROR_MESSAGE, TAG, String.class);
            }
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return GenieResponseBuilder.getErrorResponse(NetworkConstants.NETWORK_ERROR, e.getMessage(), TAG, String.class);
        }
    }

    protected abstract Map<String, String> getRequestHeaders();

    protected byte[] getRequestData() {
        return createRequestData().getBytes();
    }

    protected abstract String createRequestData();

}
