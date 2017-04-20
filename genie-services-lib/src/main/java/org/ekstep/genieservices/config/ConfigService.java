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
public class ConfigService extends BaseService {

    private static final String TAG = ConfigService.class.getSimpleName();
    private static final String TERM_JSON_FILE = "terms.json";
    private static final String RESOURCE_BUNDLE_JSON_FILE = "resource_bundle.json";
    //    private APILogger mApiLogger;

    public ConfigService(AppContext appContext) {
        super(appContext);
    }

    /**
     * Get terms
     *
     * @param type
     * @param responseHandler
     */
    public void getMasterData(MasterDataType type, IResponseHandler<String> responseHandler) {

        GenieResponse<String> response = new GenieResponse<>();

        if (getLong(Constants.MASTER_DATA_API_EXPIRATION_KEY) == 0) {
            initializeMasterData();
        } else {
            if (isExpired(Constants.MASTER_DATA_API_EXPIRATION_KEY)) {
                refreshMasterData();
            }
        }

        MasterData term = MasterData.findByType(mAppContext, type.getValue());

        String result = term.getTermJson();

        prepareResourceBundleResponse(responseHandler, response, result);
    }

    private void initializeMasterData() {

        //get the string data from the locally stored json
        String storedData = ResourcesReader.readFile(TERM_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);

            Map result = convertToMap((LinkedTreeMap) map.get("result"));

            //save the master data
            if (result != null) {
                result.remove("ttl");
                for (Object keys : result.keySet()) {
                    MasterData eachMasterData = MasterData.write(mAppContext, (String) keys, String.valueOf(new Gson().toJson(result.get(keys))));
                    eachMasterData.save();
                }
            }
        }

        refreshMasterData();

    }

    private void refreshMasterData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO Call Term API
            }
        }).start();
    }

    /**
     * Get resource bundles
     *
     * @param languageIdentifier
     * @param responseHandler
     */
    public void getResourceBundle(String languageIdentifier, IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        if (getLong(Constants.RESOURCE_BUNDLE_API_EXPIRATION_KEY) == 0) {
            initializeResourceBundle();
        } else {
            if (isExpired(Constants.RESOURCE_BUNDLE_API_EXPIRATION_KEY)) {
                refreshResourceBundle();
            }
        }

        ResourceBundle resourceBundle = ResourceBundle.findById(mAppContext, languageIdentifier);

        //get the resource bundle in string format
        String result = resourceBundle.getResourceString();

        prepareResourceBundleResponse(responseHandler, response, result);

    }

    private void initializeResourceBundle() {
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

            refreshResourceBundle();
        }
    }

    private void refreshResourceBundle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 18/4/17 Make Server Call to get the resource bundle
            }
        }).start();
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
