package org.ekstep.genieservices;

import com.google.gson.internal.LinkedTreeMap;

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

    protected boolean isExpired(String key) {
        Long currentTime = DateUtil.getEpochTime();
        long expirationTime = getLong(key);
        return currentTime > expirationTime;
    }

    protected void saveDataExpirationTime(Double ttl, String key) {
//        Double ttl = (Double) result.get("ttl");
        if (ttl != null) {
            long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
            Long currentTime = DateUtil.getEpochTime();
            long expiration_time = ttlInMilliSeconds + currentTime;

            putLong(key, expiration_time);
        }
    }

    protected void putBoolean(String key, boolean value) {
        mAppContext.getKeyValueStore().putBoolean(key, value);
    }

    protected boolean getBoolean(String key) {
        return mAppContext.getKeyValueStore().getBoolean(key, false);
    }

    protected void putLong(String key, long value) {
        mAppContext.getKeyValueStore().putLong(key, value);
    }

    protected long getLong(String key) {
        return mAppContext.getKeyValueStore().getLong(key, 0);
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
