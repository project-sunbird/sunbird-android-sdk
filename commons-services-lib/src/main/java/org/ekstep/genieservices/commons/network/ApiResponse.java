package org.ekstep.genieservices.commons.network;

/**
 * Created on 20/4/17.
 */

public class ApiResponse {
    private boolean successful;
    private String responseBody;

    public ApiResponse(boolean status, String responseBody) {
        this.successful = status;
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
