package org.ekstep.genieservices.commons.network;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    public ApiResponse doPost(IRequestBody requestBody) throws IOException {
        requestBuilder.post(prepareRequestBody(requestBody));
        Request request = requestBuilder.build();
        Response response = mHttpClient.newCall(request).execute();
        ApiResponse apiResponse = new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
        response.close();
        return apiResponse;
    }

    @Override
    public ApiResponse doPatch(IRequestBody requestBody) throws IOException {
        requestBuilder.patch(prepareRequestBody(requestBody));
        Request request = requestBuilder.build();
        Response response = mHttpClient.newCall(request).execute();
        ApiResponse apiResponse = new ApiResponse(response.isSuccessful(), response.body() != null ? response.body().string() : "", response.code());
        response.close();
        return apiResponse;
    }

    private RequestBody prepareRequestBody(IRequestBody requestBody) {

        if (requestBody.getContentType().equals(IRequestBody.MIME_TYPE_JSON)) {
            return RequestBody.create(MediaType.parse(IRequestBody.MIME_TYPE_JSON), (byte[]) requestBody.getBody());
        } else if (requestBody.getContentType().equals(IRequestBody.MIME_TYPE_FORM)) {
            Map<String, String> formData = (Map<String, String>) requestBody.getBody();
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<String> iterator = formData.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = formData.get(key);
                builder.add(key, value);
            }
            return builder.build();
        } else if (requestBody.getContentType().equals(IRequestBody.MIME_TYPE_FORM_MULTIPART)) {
            Map<String, Object> formData = (Map<String, Object>) requestBody.getBody();
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            Iterator<String> iterator = formData.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = formData.get(key);
                if (value instanceof String) {
                    builder.addFormDataPart(key, (String) value);
                } else if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                }
            }
            return builder.build();
        }
        return null;
    }

}
