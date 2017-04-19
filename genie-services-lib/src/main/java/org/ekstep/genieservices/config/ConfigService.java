package org.ekstep.genieservices.config;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.commons.utils.TimeUtil;
import org.ekstep.genieservices.config.db.model.ResourceBundle;
import org.ekstep.genieservices.config.db.model.MasterData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import util.Constants;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService {

    private static final String TAG = ConfigService.class.getSimpleName();
    //    private APILogger mApiLogger;
    private AppContext appContext;

    public ConfigService(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Save the master details
     */
    public void saveMasterData(IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        MasterData term = MasterData.findById(appContext);

        if (!term.exists()) {
            //get the string data from the locally stored json
            String storedData = appContext.getStoredResourceData();

            if (!StringUtil.isNullOrEmpty(storedData)) {
                LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);

                Map result = convertToMap((LinkedTreeMap) map.get("result"));

                //save the master data
                if (result != null) {
                    result.remove("ttl");
                    for (Object keys : result.keySet()) {
                        MasterData eachTerm = new MasterData(appContext, (String) keys, String.valueOf(new Gson().toJson(result.get(keys))));
                        eachTerm.save();
                    }
                }
            }

            response.setStatus(true);

            responseHandler.onSuccess(response);

            //TODO Call Term API
        } else {
            if (isMasterDataExpired()) {
                //TODO Call Term API
            }

            response.setStatus(true);

            responseHandler.onSuccess(response);
        }

    }

    /**
     * Get terms
     *
     * @param type
     * @param responseHandler
     */
    public void getMasterData(MasterDataType type, IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        MasterData term = MasterData.find(appContext, type.getValue());

        String result = term.getTermJson();

        prepareResourceBundleResponse(responseHandler, response, result);
    }

    /**
     * Get resource bundles
     *
     * @param languageIdentifier
     * @param responseHandler
     */
    public void getResourceBundle(String languageIdentifier, IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        ResourceBundle resourceBundle = ResourceBundle.findById(appContext, languageIdentifier);

        if (resourceBundle == null) {
            //get the string data from the locally stored json
            String storedData = appContext.getStoredResourceData();

            if (!StringUtil.isNullOrEmpty(storedData)) {
                LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);

                //save the bundle data
                Map result = convertToMap((LinkedTreeMap) map.get("result"));

                if (result != null) {
                    for (Object keys : result.keySet()) {
                        ResourceBundle eachResourceBundle = new ResourceBundle(appContext, (String) keys, String.valueOf(new Gson().toJson(result.get(keys))));
                        eachResourceBundle.save();
                    }
                }
            }

            //re-fetch the bundle data that was saved just now
            resourceBundle = ResourceBundle.findById(appContext, languageIdentifier);

            if (resourceBundle != null) {
                String result = resourceBundle.getResourceString();

                prepareResourceBundleResponse(responseHandler, response, result);
            }

            // TODO: 18/4/17 Make Server Call to get the resource bundle
        } else {
            //check if the data has expired
            if (isResourceBundleExpired()) {
                // TODO: 18/4/17 Make Server Call to get the resource bundle
            }

            //get the resource bundle in string format
            String result = resourceBundle.getResourceString();

            prepareResourceBundleResponse(responseHandler, response, result);
        }
    }

    /**
     * Prepares the response to be given back to caller
     *
     * @param responseHandler
     * @param response
     * @param result
     */
    private void prepareResourceBundleResponse(IResponseHandler<String> responseHandler, GenieResponse<String> response, String result) {
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
     * Check if master data has expired
     *
     * @return
     */
    private boolean isMasterDataExpired() {
        Long currentTime = TimeUtil.getEpochTime();
        long expirationTime = appContext.getPreferenceCache().getLong(Constants.MASTER_DATA_API_EXPIRATION_KEY, 0);
        return currentTime > expirationTime;
    }

    /**
     * Check if resource bundle has expired
     *
     * @return
     */
    private boolean isResourceBundleExpired() {
        Long currentTime = TimeUtil.getEpochTime();
        long expirationTime = appContext.getPreferenceCache().getLong(Constants.RESOURCE_BUNDLE_API_EXPIRATION_KEY, 0);
        return currentTime > expirationTime;
    }

    /**
     * Converts the LinkedTreeMap to Map
     *
     * @param result
     * @return
     * @throws IOException
     */
    private Map convertToMap(LinkedTreeMap result) {
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
