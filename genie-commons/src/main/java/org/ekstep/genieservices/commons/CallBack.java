package org.ekstep.genieservices.commons;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public interface CallBack<T> {

    void onSuccess(GenieResponse<T> genieResponse);

    void onError(GenieResponse<T> genieResponse);

}
