package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.Collections;

/**
 * Created on 9/5/17.
 */

public class GenieResponseBuilder {

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
        Logger.e(logTag, error + ":" + errorMessage);
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
        Logger.e(logTag, error + ":" + errorMessage);
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
}
