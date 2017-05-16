package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This interface is used for sending a response to the caller of the API
 * <p>
 * <p>
 * onSuccess - This callback is used when the api succeeds
 * <p>
 * <p>
 * onError - This callback is used when the api fails
 */
public interface IResponseHandler<T> {

    /**
     * This callback is used when the api succeeds
     *
     * @param genieResponse {@link GenieResponse} will be set on success of the API called
     */
    void onSuccess(GenieResponse<T> genieResponse);

    /**
     * This callback is used when the api fails
     *
     * @param genieResponse {@link GenieResponse} will be set on failure of the API called
     */
    void onError(GenieResponse<T> genieResponse);

}
