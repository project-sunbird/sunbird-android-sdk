package org.ekstep.genieservices;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.db.cache.IKeyValueOperation;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 20/4/17.
 */

public class BaseService {

    protected AppContext mAppContext;
    private IKeyValueOperation mkeyValueStore;

    public BaseService(AppContext appContext) {
        mAppContext = appContext;
    }

    private IKeyValueOperation getKeyValueStore() {
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

    /**
     * Converts the LinkedTreeMap to Map
     *
     * @param result
     * @return
     * @throws IOException
     */
    protected Map convertToMap(LinkedTreeMap result) {
        Map mapResourceBundleData;
        Gson gson = new Gson();
//        saveDataExpirationTime(result);
        String resultDataString = null;
        if (result.containsKey("resourcebundles")) {
            resultDataString = gson.toJson(result.get("resourcebundles"));
        }
        Type type = new TypeToken<HashMap>() {
        }.getType();
        mapResourceBundleData = gson.fromJson(resultDataString, type);

        return mapResourceBundleData;
    }
}
