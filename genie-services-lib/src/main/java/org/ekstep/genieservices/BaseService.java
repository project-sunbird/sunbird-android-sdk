package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.utils.DateUtil;

/**
 * Created by swayangjit on 20/4/17.
 */

public class BaseService {

    protected AppContext mAppContext;
    private IKeyValueStore mkeyValueStore;

    public BaseService(AppContext appContext) {
        mAppContext = appContext;
    }

    private IKeyValueStore getKeyValueStore() {
        if (mkeyValueStore == null) {
            mkeyValueStore = mAppContext.getKeyValueStore();
        }
        return mkeyValueStore;
    }

    protected boolean isExpired(String key) {
        Long currentTime = DateUtil.getEpochTime();
        long expirationTime = getLong(key);
        return currentTime > expirationTime;
    }


    protected void putBoolean(String key, boolean value) {
        getKeyValueStore().putBoolean(key, value);
    }

    protected boolean getBoolean(String key) {
        return getKeyValueStore().getBoolean(key, false);
    }

    protected void putLong(String key, long value) {
        getKeyValueStore().putLong(key, value);
    }

    protected long getLong(String key) {
        return getKeyValueStore().getLong(key, 0);
    }

    /**
     * Prepares the response to be given back to caller
     *
     * @param responseHandler
     * @param response
     * @param result
     */
    protected void prepareResponse(IResponseHandler<String> responseHandler, GenieResponse<String> response, String result) {
        if (result != null) {
            response.setStatus(true);
            response.setResult(result);
            responseHandler.onSuccess(response);
        } else {
            response.setStatus(false);
            responseHandler.onError(response);
        }
    }

}
