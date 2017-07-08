package org.ekstep.genieservices.commons.network;

/**
 * Created on 20/4/17.
 */

public class ApiResponse {
    private boolean successful;
    private int responseCode;
    private String responseBody;

    public ApiResponse(boolean status, String responseBody, int responseCode) {
        this.successful = status;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
