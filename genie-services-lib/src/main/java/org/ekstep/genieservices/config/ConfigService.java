package org.ekstep.genieservices.config;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.commons.utils.TimeUtil;
import org.ekstep.genieservices.config.db.model.MasterData;
import org.ekstep.genieservices.config.db.model.ResourceBundle;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import util.Constants;
import util.ResourcesReader;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService extends BaseService{

    private static final String TAG = ConfigService.class.getSimpleName();
    private static final String TERM_JSON_FILE = "terms.json";
    private static final String RESOURCE_BUNDLE_JSON_FILE = "resource_bundle.json";
    //    private APILogger mApiLogger;

    public ConfigService(AppContext appContext) {
       super(appContext);
    }

    /**
     * Save the master details
     */
    private  void saveMasterData() {

        if (getBoolean(Constants.IS_MASTER_DATA_SAVED_KEY)) {
            //get the string data from the locally stored json
            String storedData = ResourcesReader.readFile(TERM_JSON_FILE);

            if (!StringUtil.isNullOrEmpty(storedData)) {
                LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);

                Map result = convertToMap((LinkedTreeMap) map.get("result"));

                //save the master data
                if (result != null) {
                    result.remove("ttl");
                    for (Object keys : result.keySet()) {
                        MasterData eachMasterData =MasterData.write(mAppContext, (String) keys, String.valueOf(new Gson().toJson(result.get(keys))));
                        eachMasterData.save();
                    }
                    putBoolean(Constants.IS_MASTER_DATA_SAVED_KEY,true);
                }
            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    //TODO Call Term API
                }
            }).start();

        } else {
            if (isMasterDataExpired()) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO Call Term API
                    }
                }).start();
            }

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

        saveMasterData();

        MasterData term = MasterData.findByType(mAppContext, type.getValue());

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

        ResourceBundle resourceBundle = ResourceBundle.findById(mAppContext, languageIdentifier);

        if (resourceBundle == null) {
            //get the string data from the locally stored json
            String storedData = ResourcesReader.readFile(RESOURCE_BUNDLE_JSON_FILE);

            if (!StringUtil.isNullOrEmpty(storedData)) {
                LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);

                //save the bundle data
                Map result = convertToMap((LinkedTreeMap) map.get("result"));

                if (result != null) {
                    for (Object keys : result.keySet()) {
                        ResourceBundle eachResourceBundle = ResourceBundle.write(mAppContext, (String) keys, String.valueOf(new Gson().toJson(result.get(keys))));
                        eachResourceBundle.save();
                    }
                }
            }

            //re-fetch the bundle data that was saved just now
            resourceBundle = ResourceBundle.findById(mAppContext, languageIdentifier);

            if (resourceBundle != null) {
                String result = resourceBundle.getResourceString();

                prepareResourceBundleResponse(responseHandler, response, result);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO: 18/4/17 Make Server Call to get the resource bundle
                }
            }).start();


        } else {
            //check if the data has expired
            if (isResourceBundleExpired()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 18/4/17 Make Server Call to get the resource bundle
                    }
                }).start();
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
        long expirationTime = getLong(Constants.MASTER_DATA_API_EXPIRATION_KEY);
        return currentTime > expirationTime;
    }

    /**
     * Check if resource bundle has expired
     *
     * @return
     */
    private boolean isResourceBundleExpired() {
        Long currentTime = TimeUtil.getEpochTime();
        long expirationTime = getLong(Constants.RESOURCE_BUNDLE_API_EXPIRATION_KEY);
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
