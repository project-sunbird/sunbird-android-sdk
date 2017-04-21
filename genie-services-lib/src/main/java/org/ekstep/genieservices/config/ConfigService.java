package org.ekstep.genieservices.config;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.db.model.MasterData;
import org.ekstep.genieservices.config.db.model.Ordinals;
import org.ekstep.genieservices.config.db.model.ResourceBundle;
import org.ekstep.genieservices.config.network.OrdinalsAPI;

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
    private static final String ORDINALS_JSON_FILE = "ordinals.json";

    private static final String DB_KEY_ORDINALS = "ordinals_key";
    //    private APILogger mApiLogger;

    public ConfigService(AppContext appContext) {
        super(appContext);
    }

    public void getAllMasterData() {
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

        prepareResponse(responseHandler, response, result);
    }

    private void initializeMasterData() {

        //get the string data from the locally stored json
        String storedData = ResourcesReader.readFile(TERM_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            LinkedTreeMap map = GsonUtil.toMap(storedData, LinkedTreeMap.class);

            Map result = ((LinkedTreeMap) map.get("result"));

            //save the master data
            if (result != null) {
                result.remove("ttl");
                for (Object keys : result.keySet()) {
                    MasterData eachMasterData = MasterData.create(mAppContext, (String) keys, GsonUtil.toJson(result.get(keys)));
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

        prepareResponse(responseHandler, response, result);

    }

    private void initializeResourceBundle() {
        //get the string data from the locally stored json
        String storedData = ResourcesReader.readFile(RESOURCE_BUNDLE_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            LinkedTreeMap map = GsonUtil.toMap(storedData, LinkedTreeMap.class);

            //save the bundle data
            Map mMapResource = (LinkedTreeMap) map.get("result");
            Map result = null;


            String resultDataString = null;
            if (mMapResource.containsKey("resourcebundles")) {
                resultDataString = GsonUtil.toString(mMapResource.get("resourcebundles"));
                result = GsonUtil.toMap(resultDataString, Map.class);
            }

            if (result != null) {
                for (Object keys : result.keySet()) {
                    ResourceBundle eachResourceBundle = ResourceBundle.create(mAppContext, (String) keys, GsonUtil.toJson(result.get(keys)));
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

    public void getOrdinals(IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();

        if (getLong(Constants.ORDINAL_API_EXPIRATION_KEY) == 0) {
            initializeOrdinalsDate();
        } else {
            if (isExpired(Constants.ORDINAL_API_EXPIRATION_KEY)) {
                refreshOrdinals();
            }
        }

        Ordinals ordinals = Ordinals.findById(mAppContext, DB_KEY_ORDINALS);

        prepareResponse(responseHandler, response, ordinals.getJSON());
    }

    private void initializeOrdinalsDate() {
        //get the string data from the locally stored json
        String storedData = ResourcesReader.readFile(ORDINALS_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            LinkedTreeMap map = new Gson().fromJson(storedData, LinkedTreeMap.class);
            LinkedTreeMap resultLinkedTreeMap = (LinkedTreeMap) map.get("result");

            if (resultLinkedTreeMap.containsKey("ordinals")) {
                Ordinals ordinals = Ordinals.create(mAppContext, DB_KEY_ORDINALS, GsonUtil.toJson(resultLinkedTreeMap.get("ordinals")));
                ordinals.save();
            }
        }

        refreshOrdinals();
    }

    private void refreshOrdinals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrdinalsAPI api = new OrdinalsAPI(mAppContext);
                GenieResponse genieResponse = api.get();

                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    LinkedTreeMap map = new Gson().fromJson(body, LinkedTreeMap.class);
                    LinkedTreeMap resultLinkedTreeMap = (LinkedTreeMap) map.get("result");

                    // Save data expiration time
                    saveDataExpirationTime(resultLinkedTreeMap, Constants.ORDINAL_API_EXPIRATION_KEY);

                    if (resultLinkedTreeMap.containsKey("ordinals")) {
                        Ordinals ordinals = Ordinals.create(mAppContext, DB_KEY_ORDINALS, GsonUtil.toJson(resultLinkedTreeMap.get("ordinals")));
                        ordinals.save();
                    }
                }
            }
        }).start();
    }

}
