package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Collections;

/**
 * Created by mathew on 9/5/17.
 */

public class GenieResponseBuilder {

    public static <T> GenieResponse<T> getErrorResponse(String error, String errorMessage, String logTag, Class<T> type) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        Logger.e(logTag, error + ":" + errorMessage);
        return response;
    }

    public static <T> GenieResponse<T> getErrorResponse(String error,String logTag) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(""));
        response.setError(error);
        Logger.e(logTag, error);
        return response;
    }

    public static <T> GenieResponse<T> getErrorResponse(String error, String errorMessage, String logTag) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(false);
        response.setErrorMessages(Collections.singletonList(errorMessage));
        response.setError(error);
        Logger.e(logTag, error + ":" + errorMessage);
        return response;
    }

    public static <T> GenieResponse<T> getSuccessResponse(String message, Class<T> type) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }

    public static <T> GenieResponse<T> getSuccessResponse(String message) {
        GenieResponse<T> response = new GenieResponse<T>();
        response.setStatus(true);
        response.setMessage(message);
        return response;
    }
}
