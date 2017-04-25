package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.db.model.MasterData;
import org.ekstep.genieservices.config.db.model.Ordinals;
import org.ekstep.genieservices.config.db.model.ResourceBundle;
import org.ekstep.genieservices.config.network.OrdinalsAPI;
import org.ekstep.genieservices.config.network.ResourceBundleAPI;
import org.ekstep.genieservices.config.network.TermsAPI;

import java.util.Map;

import org.ekstep.genieservices.commons.utils.FileUtil;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService extends BaseService {

    private static final String RESOURCE_BUNDLE_API_EXPIRATION_KEY = "RESOURCE_BUNDLE_API_EXPIRATION_KEY";
    private static final String MASTER_DATA_API_EXPIRATION_KEY = "TERMS_API_EXPIRATION_KEY";
    private static final String ORDINAL_API_EXPIRATION_KEY = "ORDINAL_API_EXPIRATION_KEY";

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

        if (getLongFromKeyValueStore(MASTER_DATA_API_EXPIRATION_KEY) == 0) {
            initializeMasterData();
        } else if (hasExpired(MASTER_DATA_API_EXPIRATION_KEY)) {
            refreshMasterData();
        }

        MasterData term = MasterData.findByType(mAppContext, type.getValue());

        String result = term.getTermJson();

        handleResponse(responseHandler, result, mAppContext);
    }

    private void initializeMasterData() {

        //get the string data from the locally stored json
        String storedData = FileUtil.readFileFromClasspath(TERM_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveMasterData(storedData);
        }

        refreshMasterData();
    }

    private void saveMasterData(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);

        Map result = ((LinkedTreeMap) map.get("result"));

        //save the master data
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, MASTER_DATA_API_EXPIRATION_KEY);
            result.remove("ttl");
            for (Object key : result.keySet()) {
                MasterData eachMasterData = MasterData.create(mAppContext, (String) key, GsonUtil.toJson(result.get(key)));
                eachMasterData.save();
            }
        }
    }

    private void refreshMasterData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TermsAPI termsAPI = new TermsAPI(mAppContext);
                GenieResponse genieResponse = termsAPI.get();
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveMasterData(body);
                }
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

        if (getLongFromKeyValueStore(RESOURCE_BUNDLE_API_EXPIRATION_KEY) == 0) {
            initializeResourceBundle();
        } else if (hasExpired(RESOURCE_BUNDLE_API_EXPIRATION_KEY)) {
            refreshResourceBundle();
        }

        ResourceBundle resourceBundle = ResourceBundle.findById(mAppContext, languageIdentifier);

        //get the resource bundle in string format
        String result = resourceBundle.getResourceString();

        handleResponse(responseHandler, result, mAppContext);

    }

    private void initializeResourceBundle() {
        //get the string data from the locally stored json
        String storedData = FileUtil.readFileFromClasspath(RESOURCE_BUNDLE_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveResourceBundle(storedData);
        }

        refreshResourceBundle();
    }

    private void saveResourceBundle(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);

        //save the bundle data
        Map resultMap = (LinkedTreeMap) map.get("result");
        Map result = null;

        if (resultMap.containsKey("resourcebundles")) {
            result = (Map) resultMap.get("resourcebundles");
        }

        if (result != null) {
            Double ttl = (Double) resultMap.get("ttl");
            saveDataExpirationTime(ttl, RESOURCE_BUNDLE_API_EXPIRATION_KEY);
            for (Object key : result.keySet()) {
                ResourceBundle eachResourceBundle = ResourceBundle.create(mAppContext, (String) key, GsonUtil.toJson(result.get(key)));
                eachResourceBundle.save();
            }
        }

    }

    private void refreshResourceBundle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResourceBundleAPI resourceBundleAPI = new ResourceBundleAPI(mAppContext);
                GenieResponse genieResponse = resourceBundleAPI.get();
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveResourceBundle(body);
                }
            }
        }).start();
    }

    public void getOrdinals(IResponseHandler<String> responseHandler) {

        if (getLongFromKeyValueStore(ORDINAL_API_EXPIRATION_KEY) == 0) {
            initializeOrdinalsDate();
        } else if (hasExpired(ORDINAL_API_EXPIRATION_KEY)) {
            refreshOrdinals();
        }

        Ordinals ordinals = Ordinals.findById(mAppContext, DB_KEY_ORDINALS);

        handleResponse(responseHandler, ordinals.getJSON(), mAppContext);
    }

    private void initializeOrdinalsDate() {
        //get the string data from the locally stored json
        String storedData = FileUtil.readFileFromClasspath(ORDINALS_JSON_FILE);

        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveOrdinals(storedData);
        }

        refreshOrdinals();
    }

    private void saveOrdinals(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        LinkedTreeMap resultLinkedTreeMap = (LinkedTreeMap) map.get("result");

        if (resultLinkedTreeMap.containsKey("ordinals")) {
            Double ttl = (Double) map.get("ttl");
            saveDataExpirationTime(ttl, ORDINAL_API_EXPIRATION_KEY);
            Ordinals ordinals = Ordinals.create(mAppContext, DB_KEY_ORDINALS, GsonUtil.toJson(resultLinkedTreeMap.get("ordinals")));
            ordinals.save();
        }
    }

    private void refreshOrdinals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrdinalsAPI api = new OrdinalsAPI(mAppContext);
                GenieResponse genieResponse = api.get();

                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveOrdinals(body);
                }
            }
        }).start();
    }

    protected void saveDataExpirationTime(Double ttl, String key) {
        if (ttl != null) {
            long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
            Long currentTime = DateUtil.getEpochTime();
            long expiration_time = ttlInMilliSeconds + currentTime;

            putToKeyValueStore(key, expiration_time);
        }
    }

    protected boolean hasExpired(String key) {
        Long currentTime = DateUtil.getEpochTime();
        long expirationTime = getLongFromKeyValueStore(key);
        return currentTime > expirationTime;
    }

}
