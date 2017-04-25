package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.utils.DateUtil;

/**
 * Created on 20/4/17.
 *
 * @author swayangjit
 */
public class BaseService {

    protected AppContext mAppContext;

    public BaseService(AppContext appContext) {
        mAppContext = appContext;
    }

    protected void putToKeyValueStore(String key, boolean value) {
        mAppContext.getKeyValueStore().putBoolean(key, value);
    }

    protected boolean getBooleanFromKeyValueStore(String key) {
        return mAppContext.getKeyValueStore().getBoolean(key, false);
    }

    protected void putToKeyValueStore(String key, long value) {
        mAppContext.getKeyValueStore().putLong(key, value);
    }

    protected long getLongFromKeyValueStore(String key) {
        return mAppContext.getKeyValueStore().getLong(key, 0);
    }

    /**
     * Prepares the response to be given back to caller
     *  @param responseHandler
     * @param result
     * @param mAppContext*/
    protected void handleResponse(IResponseHandler<String> responseHandler, String result, AppContext mAppContext) {
        GenieResponse<String> response = null;
        if (result != null) {
            response = GenieResponse.getSuccessResponse("");
            response.setResult(result);
            responseHandler.onSuccess(response);
        } else {
            response = GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NO_DATA_FOUND, "", ServiceConstants.SERVICE_ERROR);
            responseHandler.onError(response);
        }
    }

}
