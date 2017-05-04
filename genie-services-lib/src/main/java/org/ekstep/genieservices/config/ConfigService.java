package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.db.model.MasterDataModel;
import org.ekstep.genieservices.config.db.model.OrdinalsModel;
import org.ekstep.genieservices.config.db.model.ResourceBundleModel;
import org.ekstep.genieservices.config.network.OrdinalsAPI;
import org.ekstep.genieservices.config.network.ResourceBundleAPI;
import org.ekstep.genieservices.config.network.TermsAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class ConfigService extends BaseService {

    private static final String TAG = ConfigService.class.getSimpleName();

    private static final String DB_KEY_ORDINALS = "ordinals_key";

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
    public void getMasterData(MasterDataType type, IResponseHandler<MasterData> responseHandler) {
        if (getLongFromKeyValueStore(ServiceConstants.PreferenceKey.MASTER_DATA_API_EXPIRATION_KEY) == 0) {
            initializeMasterData();
        } else if (hasExpired(ServiceConstants.PreferenceKey.MASTER_DATA_API_EXPIRATION_KEY)) {
            refreshMasterData();
        }

        MasterDataModel masterDataModel = MasterDataModel.findByType(mAppContext.getDBSession(), type.getValue());

        String result = masterDataModel.getMasterDataJson();

        MasterData masterData = GsonUtil.fromJson(result, MasterData.class);

        GenieResponse<MasterData> response;
        if (result != null) {
            response = GenieResponse.getSuccessResponse("");
            response.setResult(masterData);
            responseHandler.onSuccess(response);
        } else {
            response = GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NO_DATA_FOUND, "", ServiceConstants.SERVICE_ERROR);
            responseHandler.onError(response);
        }
    }

    private void initializeMasterData() {
        String storedData = FileUtil.readFileFromClasspath(ServiceConstants.ConfigResourceFiles.MASTER_DATA_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveMasterData(storedData);
        }
        refreshMasterData();
    }

    private void saveMasterData(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        Map result = ((LinkedTreeMap) map.get("result"));
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, ServiceConstants.PreferenceKey.MASTER_DATA_API_EXPIRATION_KEY);
            result.remove("ttl");
            for (Object key : result.keySet()) {
                MasterDataModel eachMasterData = MasterDataModel.build(mAppContext.getDBSession(), (String) key, GsonUtil.toJson(result.get(key)));
                MasterDataModel masterDataInDb = MasterDataModel.findByType(mAppContext.getDBSession(), String.valueOf(key));
                if (masterDataInDb != null) {
                    eachMasterData.update();
                } else {
                    eachMasterData.save();
                }

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
    public void getResourceBundle(String languageIdentifier, IResponseHandler<Map<String, Object>> responseHandler) {
        if (getLongFromKeyValueStore(ServiceConstants.PreferenceKey.RESOURCE_BUNDLE_API_EXPIRATION_KEY) == 0) {
            initializeResourceBundle();
        } else if (hasExpired(ServiceConstants.PreferenceKey.RESOURCE_BUNDLE_API_EXPIRATION_KEY)) {
            refreshResourceBundle();
        }
        ResourceBundleModel resourceBundle = ResourceBundleModel.findById(mAppContext.getDBSession(), languageIdentifier);
        String result = resourceBundle.getResourceString();
        Map<String, Object> resourceBundleMap = new HashMap<>();
        resourceBundleMap.put(resourceBundle.getIdentifier(), resourceBundle.getResourceString());

        GenieResponse<Map<String, Object>> response;
        if (result != null) {
            response = GenieResponse.getSuccessResponse("");
            response.setResult(resourceBundleMap);
            responseHandler.onSuccess(response);
        } else {
            response = GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NO_DATA_FOUND, "", ServiceConstants.SERVICE_ERROR);
            responseHandler.onError(response);
        }
    }

    private void initializeResourceBundle() {
        String storedData = FileUtil.readFileFromClasspath(ServiceConstants.ConfigResourceFiles.RESOURCE_BUNDLE_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveResourceBundle(storedData);
        }
        refreshResourceBundle();
    }

    private void saveResourceBundle(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        Map resultMap = (LinkedTreeMap) map.get("result");
        Map result = null;
        if (resultMap.containsKey("resourcebundles")) {
            result = (Map) resultMap.get("resourcebundles");
        }
        if (result != null) {
            Double ttl = (Double) resultMap.get("ttl");
            saveDataExpirationTime(ttl, ServiceConstants.PreferenceKey.RESOURCE_BUNDLE_API_EXPIRATION_KEY);
            for (Object key : result.keySet()) {
                ResourceBundleModel eachResourceBundle = ResourceBundleModel.build(mAppContext.getDBSession(), (String) key, GsonUtil.toJson(result.get(key)));
                ResourceBundleModel resourceBundleInDb = ResourceBundleModel.findById(mAppContext.getDBSession(), String.valueOf(key));
                if (resourceBundleInDb != null) {
                    eachResourceBundle.update();
                } else {
                    eachResourceBundle.save();
                }

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

    public void getOrdinals(IResponseHandler<HashMap> responseHandler) {
        if (getLongFromKeyValueStore(ServiceConstants.PreferenceKey.ORDINAL_API_EXPIRATION_KEY) == 0) {
            initializeOrdinalsData();
        } else if (hasExpired(ServiceConstants.PreferenceKey.ORDINAL_API_EXPIRATION_KEY)) {
            refreshOrdinals();
        }
        OrdinalsModel ordinals = OrdinalsModel.findById(mAppContext.getDBSession(), DB_KEY_ORDINALS);
        HashMap ordinalsMap = GsonUtil.fromJson(ordinals.getJSON(), HashMap.class);
        GenieResponse<HashMap> response;
        if (ordinalsMap != null) {
            response = GenieResponse.getSuccessResponse("");
            response.setResult(ordinalsMap);
            responseHandler.onSuccess(response);
        } else {
            response = GenieResponse.getErrorResponse(mAppContext, ServiceConstants.NO_DATA_FOUND, "", ServiceConstants.SERVICE_ERROR);
            responseHandler.onError(response);
        }
    }

    private void initializeOrdinalsData() {
        String storedData = FileUtil.readFileFromClasspath(ServiceConstants.ConfigResourceFiles.ORDINALS_JSON_FILE);
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
            saveDataExpirationTime(ttl, ServiceConstants.PreferenceKey.ORDINAL_API_EXPIRATION_KEY);
            OrdinalsModel ordinals = OrdinalsModel.build(mAppContext.getDBSession(), DB_KEY_ORDINALS, GsonUtil.toJson(resultLinkedTreeMap.get("ordinals")));
            OrdinalsModel ordinalsInDb = OrdinalsModel.findById(mAppContext.getDBSession(), DB_KEY_ORDINALS);
            if (ordinalsInDb != null) {
                ordinals.update();
            } else {
                ordinals.save();
            }
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

    private void saveDataExpirationTime(Double ttl, String key) {
        if (ttl != null) {
            long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
            Long currentTime = DateUtil.getEpochTime();
            long expiration_time = ttlInMilliSeconds + currentTime;

            putToKeyValueStore(key, expiration_time);
        }
    }

    private boolean hasExpired(String key) {
        Long currentTime = DateUtil.getEpochTime();
        long expirationTime = getLongFromKeyValueStore(key);
        return currentTime > expirationTime;
    }

}
