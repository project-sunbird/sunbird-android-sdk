package org.ekstep.genieservices.commons.network;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created on 20/4/17.
 */

public class AndroidHttpClient implements IHttpClient {

    private OkHttpClient mHttpClient;
    private Request.Builder requestBuilder;

    public AndroidHttpClient(OkHttpClient httpClient) {
        this.mHttpClient = httpClient;
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
        ApiResponse apiResponse = new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
        response.close();
        return apiResponse;
    }

    @Override
    public ApiResponse doPost(byte[] requestBody) throws IOException {
        requestBuilder.post(RequestBody.create(MediaType.parse("application/json"), requestBody));
        Request request = requestBuilder.build();
        Response response = mHttpClient.newCall(request).execute();
        ApiResponse apiResponse = new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
        response.close();
        return apiResponse;
    }

}
