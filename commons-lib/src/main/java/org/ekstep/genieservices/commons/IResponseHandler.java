package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public interface IResponseHandler<T> {

    void onSuccess(GenieResponse<T> genieResponse);

    void onError(GenieResponse<T> genieResponse);

}
