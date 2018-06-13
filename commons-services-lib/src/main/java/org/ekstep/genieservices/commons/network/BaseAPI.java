package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.auth.AuthHandler;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;

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
    private static final String PATCH = "PATCH";
    private static final int AUTHENTICATION_FAILURE = 401;

    private AppContext mAppContext;
    private IHttpClientFactory httpClientFactory;
    private Map<String, String> headers;
    private String url;
    private String TAG;

    public BaseAPI(AppContext appContext, String url, String TAG) {
        this.url = url;
        this.mAppContext = appContext;
        this.TAG = TAG;
        this.httpClientFactory = appContext.getHttpClientFactory();
        this.headers = new HashMap<>();
        this.headers.put("Accept-Encoding", "gzip, deflate");
        this.headers.put("X-Channel-Id", mAppContext.getParams().getString(IParams.Key.CHANNEL_ID));
        this.headers.put("X-App-Id", mAppContext.getParams().getString(IParams.Key.PRODUCER_ID));
        this.headers.put("X-Device-Id", mAppContext.getDeviceInfo().getDeviceID());
    }

    public GenieResponse get() {
        return fetchFromServer(GET, true);
    }

    public GenieResponse post() {
        return fetchFromServer(POST, true);
    }

    public GenieResponse patch() {
        return fetchFromServer(PATCH, true);
    }

    protected void processAuthFailure(ApiResponse apiResponse) {
        AuthHandler.resetAuthToken(mAppContext);
    }


    private GenieResponse fetchFromServer(String requestType, boolean retryForAuthError) {
        if (!mAppContext.getConnectionInfo().isConnected()) {
            return getErrorResponse(NetworkConstants.CONNECTION_ERROR, NetworkConstants.CONNECTION_ERROR_MESSAGE);
        }
        try {
            ApiResponse apiResponse = invokeApi(requestType);
            if (apiResponse.isSuccessful()) {
                return getSuccessResponse(apiResponse.getResponseBody());
            } else if (apiResponse.getResponseCode() == AUTHENTICATION_FAILURE) {
                if (retryForAuthError) {
                    processAuthFailure(apiResponse);
                    return fetchFromServer(requestType, false);
                } else {
                    String error = NetworkConstants.SERVERAUTH_ERROR;
                    String errorMsg = NetworkConstants.SERVERAUTH_ERROR_MESSAGE;
                    if (!StringUtil.isNullOrEmpty(apiResponse.getResponseBody())) {
                        errorMsg = apiResponse.getResponseBody();
                    }

                    return getErrorResponse(error, errorMsg);
                }
            } else {
                String error = NetworkConstants.SERVER_ERROR;
                String errorMsg = NetworkConstants.SERVER_ERROR_MESSAGE;
                if (!StringUtil.isNullOrEmpty(apiResponse.getResponseBody())) {
                    try {
                        Map<String, Object> errorResponseBodyMap = GsonUtil.fromJson(apiResponse.getResponseBody(), Map.class);
                        if (errorResponseBodyMap != null && !errorResponseBodyMap.isEmpty()
                                && errorResponseBodyMap.containsKey("params")) {
                            Map<String, Object> params = (Map<String, Object>) errorResponseBodyMap.get("params");
                            error = (String) params.get("err");
                            errorMsg = (String) params.get("errmsg");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return getErrorResponse(error, errorMsg);
            }
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return getErrorResponse(NetworkConstants.NETWORK_ERROR, e.getMessage());
        }
    }

    private ApiResponse invokeApi(String requestType) throws IOException {
        IHttpClient httpClient = prepareClient();
        ApiResponse apiResponse = null;
        if (GET.equals(requestType)) {
            apiResponse = httpClient.doGet();
        } else if (POST.equals(requestType)) {
            apiResponse = httpClient.doPost(getRequestBody());
        } else if (PATCH.equals(requestType)) {
            apiResponse = httpClient.doPatch(getRequestBody());
        }
        return apiResponse;
    }

    private IHttpClient prepareClient() {
        IHttpClient httpClient = httpClientFactory.getClient();
        httpClient.createRequest(url);
        if (shouldAuthenticate()) {
            IHttpAuthenticator authenticator = httpClientFactory.getHttpAuthenticator();
            httpClient.setHeaders(authenticator.getAuthHeaders());
        }
        httpClient.setHeaders(headers);
        httpClient.setHeaders(getRequestHeaders());
        return httpClient;
    }

    private GenieResponse<String> getSuccessResponse(String responseBody) {
        GenieResponse<String> response = GenieResponseBuilder.getSuccessResponse("", String.class);
        response.setResult(responseBody);
        return response;
    }

    private GenieResponse<String> getErrorResponse(String error, String errorMessage) {
        return GenieResponseBuilder.getErrorResponse(error, errorMessage, TAG, String.class);
    }

    protected IRequestBody getRequestBody() {
        IRequestBody requestBody = new ByteArrayRequestBody();
        requestBody.setBody(getRequestData());
        return requestBody;
    }

    protected boolean shouldAuthenticate() {
        return true;
    }

    protected abstract Map<String, String> getRequestHeaders();

    protected byte[] getRequestData() {
        return createRequestData().getBytes();
    }

    protected abstract String createRequestData();

}
