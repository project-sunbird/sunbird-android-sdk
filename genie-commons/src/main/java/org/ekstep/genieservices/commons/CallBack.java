package org.ekstep.genieservices.commons;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public interface CallBack<T> {

    void onSuccess(Response<T> response);

    void onError(Response<T> response);

}
