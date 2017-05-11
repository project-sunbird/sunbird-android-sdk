package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class will be used to return a response to the caller in GenieResponse<T> object.
 */
public class GenieResponse<T> {

    private String message;
    private T result;
    private boolean status;
    private List<String> errorMessages;
    private String error;

    /**
     * Gets the result of the api
     *
     * @return The result data will be represented in the form of caller expectations using generics
     */
    public T getResult() {
        return this.result;
    }

    /**
     * Set the result of the api
     *
     * @param result The result data will be set in the form of caller expectations using generics
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * Gets the error type
     *
     * @return
     */
    public String getError() {
        return this.error;
    }

    /**
     * Set the error type
     *
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets the status of the response, which can be true/false based on the success/failure of the API
     *
     * @return
     */
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Set the status of the response, which can be true/false based on the success/failure of the API
     *
     * @return
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Gets the list of error messages
     *
     * @return
     */
    public List<String> getErrorMessages() {
        return this.errorMessages;
    }

    /**
     * Set the list of error messages
     *
     * @param errorMessages
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * Gets the general message which was set by API
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set the message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
