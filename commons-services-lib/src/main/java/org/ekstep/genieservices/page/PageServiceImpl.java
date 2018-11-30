package org.ekstep.genieservices.page;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IPageService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PageAssemble;
import org.ekstep.genieservices.commons.bean.PageAssembleCriteria;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.page.network.PageAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageServiceImpl extends BaseService implements IPageService {

    private static final String TAG = PageServiceImpl.class.getSimpleName();

    private static final String KEY_PAGE_ASSEMBLE = "pageAssemble-";

    private static final Double DEFAULT_TTL = 3d;   // In hours


    public PageServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<PageAssemble> getPageAssemble(PageAssembleCriteria pageAssembleCriteria) {
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(pageAssembleCriteria));
        params.put("logLevel", "2");
        String methodName = "getPageAssemble@PageServiceImpl";

        pageAssembleCriteria.getFilters().setCompatibilityLevel(getCompatibilityLevelFilter(mAppContext));
        String key = getKeyForDB(pageAssembleCriteria);

        //get the expiration time to check if the cached data has expired
        long expirationTime = getLongFromKeyValueStore(key);

        GenieResponse<PageAssemble> response;

        //check if is their any data stored in DB, for the key we have generated
        NoSqlModel pageData = NoSqlModel.findByKey(mAppContext.getDBSession(), key);

        if (pageData == null) {
            GenieResponse pageAssembleResponse = invokeAPI(pageAssembleCriteria);
            if (pageAssembleResponse.getStatus()) {
                String jsonResponse = pageAssembleResponse.getResult().toString();
                pageData = NoSqlModel.build(mAppContext.getDBSession(), key, jsonResponse);
                savePageData(pageData.getValue(), pageAssembleCriteria);
            } else {
                response = GenieResponseBuilder.getErrorResponse(pageAssembleResponse.getError(),
                        pageAssembleResponse.getMessage(), TAG);

                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, pageAssembleResponse.getMessage());
                return response;
            }
        } else if (hasExpired(expirationTime)) {
            refreshPageData(pageAssembleCriteria);
        }

        LinkedTreeMap map = GsonUtil.fromJson(pageData.getValue(), LinkedTreeMap.class);

        PageAssemble pageAssemble = null;

        if (map != null && map.get("result") != null) {
            LinkedTreeMap responseMap = (LinkedTreeMap) ((LinkedTreeMap) map.get("result")).get("response");

            if (responseMap != null) {
                pageAssemble = new PageAssemble();
                pageAssemble.setId((String) responseMap.get("id"));
                pageAssemble.setName((String) responseMap.get("name"));
                pageAssemble.setSections(GsonUtil.toJson(responseMap.get("sections")));
            }
        }

        if (pageAssemble != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(pageAssemble);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_PAGE_DATA_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PAGE, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_PAGE_DATA);
        }

        return response;
    }

    private String getKeyForDB(PageAssembleCriteria pageAssembleCriteria) {
        return KEY_PAGE_ASSEMBLE
                + mAppContext.getParams().getString(IParams.Key.PAGE_SERVICE_BASE_URL)
                + pageAssembleCriteria.getName()
                + pageAssembleCriteria.getMode()
                + pageAssembleCriteria.getFilters().toString();
    }

    private boolean hasExpired(long expirationTime) {
        Long currentTime = DateUtil.getEpochTime();
        return currentTime > expirationTime;
    }

    private void refreshPageData(final PageAssembleCriteria pageAssembleCriteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse pageAssembleResponse = invokeAPI(pageAssembleCriteria);
                if (pageAssembleResponse.getStatus()) {
                    String jsonResponse = pageAssembleResponse.getResult().toString();
                    savePageData(jsonResponse, pageAssembleCriteria);
                }
            }
        }).start();
    }

    private GenieResponse invokeAPI(PageAssembleCriteria pageAssembleCriteria) {
        PageAPI pageAPI = new PageAPI(mAppContext, pageAssembleCriteria);

        return pageAPI.post();
    }

    private void savePageData(String response, PageAssembleCriteria pageAssembleCriteria) {
        String key = getKeyForDB(pageAssembleCriteria);
        NoSqlModel pageData = NoSqlModel.build(mAppContext.getDBSession(), key, response);
        Map map = GsonUtil.fromJson(pageData.getValue(), Map.class);
        if (map != null) {
            Map result = (Map) map.get("result");
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, key);
        }

        NoSqlModel pageDataInDB = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        if (pageDataInDB == null) {
            pageData.save();
        } else {
            pageData.update();
        }
        pageData.save();
    }

    private void saveDataExpirationTime(Double ttl, String key) {
        if (ttl == null || ttl == 0) {
            ttl = DEFAULT_TTL;
        }
        long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
        Long currentTime = DateUtil.getEpochTime();
        long expiration_time = ttlInMilliSeconds + currentTime;

        mAppContext.getKeyValueStore().putLong(key, expiration_time);
    }

    private Map<String, Integer> getCompatibilityLevelFilter(AppContext appContext) {
        Map<String, Integer> compatibilityLevelMap = new HashMap<>();
        compatibilityLevelMap.put("min", appContext.getParams().getInt(IParams.Key.MIN_COMPATIBILITY_LEVEL));
        compatibilityLevelMap.put("max", appContext.getParams().getInt(IParams.Key.MAX_COMPATIBILITY_LEVEL));
        return compatibilityLevelMap;
    }

}
