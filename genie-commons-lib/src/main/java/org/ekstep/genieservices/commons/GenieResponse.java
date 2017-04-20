package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class GenieResponse<T> {

    private String message;
    private T result;
    private boolean status;
    private List<String> errorMessages;
    private String error;


    public static GenieResponse getErrorResponse(AppContext context, String error, String errorMessage, String logTag) {
        GenieResponse response = new GenieResponse();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        Logger.e(context, logTag, error + ":" + errorMessage);
        return response;
    }

    public static GenieResponse getSuccessResponse(String message) {
        GenieResponse response = new GenieResponse();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getErrorMessages() {
        return this.errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
