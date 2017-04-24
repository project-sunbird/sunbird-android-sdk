package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.db.model.MasterData;
import org.ekstep.genieservices.config.db.model.ResourceBundle;

import java.util.Map;

import util.Constants;
import org.ekstep.genieservices.commons.utils.FileUtil;

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

        prepareResponse(responseHandler, response, result);
    }

    private void initializeMasterData() {

        //get the string data from the locally stored json
        String storedData = FileUtil.readFileFromClasspath(TERM_JSON_FILE);

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
        String storedData = FileUtil.readFileFromClasspath(RESOURCE_BUNDLE_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            LinkedTreeMap map = GsonUtil.toMap(storedData, LinkedTreeMap.class);

            //save the bundle data
            Map mMapResource = (LinkedTreeMap) map.get("result");
            Map result = null;


            String resultDataString = null;
            if (mMapResource.containsKey("resourcebundles")) {
                resultDataString = GsonUtil.toString(mMapResource.get("resourcebundles"));
                result = GsonUtil.toMap(resultDataString);
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
}
