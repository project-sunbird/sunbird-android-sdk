package org.ekstep.genieresolvers;

import android.content.ContentResolver;
import android.content.Context;

import org.ekstep.genieresolvers.util.Constants;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Collections;

public abstract class BaseTask {
    protected ContentResolver contentResolver;
    private Context context;

    public BaseTask(Context context) {
        this.context = context;
    }

    /**
     * It returns the error {@link GenieResponse} object.
     *
     * @param error
     * @param errorMessage
     * @param logTag
     * @param type
     * @param <T>
     * @return
     */
    public static <T> GenieResponse<T> getErrorResponse(String error, String errorMessage, String logTag, Class<T> type) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        return response;
    }

    /**
     * It returns the error {@link GenieResponse} object.
     *
     * @param error
     * @param errorMessage
     * @param logTag
     * @param <T>
     * @return
     */
    public static <T> GenieResponse<T> getErrorResponse(String error, String errorMessage, String logTag) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        return response;
    }

    /**
     * It returns the success {@link GenieResponse} object.
     *
     * @param message
     * @param type
     * @param <T>
     * @return
     */
    public static <T> GenieResponse<T> getSuccessResponse(String message, Class<T> type) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    /**
     * It returns the success {@link GenieResponse} object.
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> GenieResponse<T> getSuccessResponse(String message) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    protected abstract String getLogTag();

    protected abstract GenieResponse execute();

    protected abstract String getErrorMessage();

    protected GenieResponse perform() {
        try {
            contentResolver = context.getContentResolver();
            if (contentResolver == null) {
                String logMessage = "Content Resolver for games not resolved";
                String errorMessage = "Not able to resolve content provider, " + getErrorMessage();
                GenieResponse errorResponse = getErrorResponse(Constants.PROCESSING_ERROR, errorMessage, logMessage);
                return errorResponse;
            }
            return execute();
        } catch (IllegalArgumentException e) {
            String errorMessage = "Latest Genie is not installed";
            String logMessage = getErrorMessage() + ", because latest genie is not installed";
            GenieResponse errorResponse = getErrorResponse(Constants.GENIE_SERVICE_NOT_INSTALLED, errorMessage, logMessage);
            return errorResponse;
        }
    }
}
