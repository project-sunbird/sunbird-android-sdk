package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.IFrameworkService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Channel;
import org.ekstep.genieservices.commons.bean.ChannelDetailsRequest;
import org.ekstep.genieservices.commons.bean.Framework;
import org.ekstep.genieservices.commons.bean.FrameworkDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.network.ChannelDetailsAPI;
import org.ekstep.genieservices.config.network.FrameworkDetailsApi;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the implementation of the interface {@link IFrameworkService}
 *
 * @author indraja
 */
public class FrameworkServiceImpl extends BaseService implements IFrameworkService {

    private static final String TAG = FrameworkServiceImpl.class.getSimpleName();

    private IAuthSession<Session> authSession;

    public FrameworkServiceImpl(AppContext appContext, IAuthSession<Session> authSession) {
        super(appContext);
        this.authSession = authSession;
    }

    private static Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
    }

    @Override
    public GenieResponse<Channel> getChannelDetails(final ChannelDetailsRequest channelDetailsRequest) {
        String methodName = "getChannelDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(channelDetailsRequest));

        long expirationTime = getLongFromKeyValueStore(FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY);
        String channelId = channelDetailsRequest.getChannelId();
        if (expirationTime == 0) {
            initializeChannelDetails(channelId);
        } else if (hasExpired(expirationTime)) {
            refreshChannelDetails(channelId);
        }

        NoSqlModel refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), String.valueOf(FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY));

        Channel channelDetails = null;
        if (refreshDetailsInDb != null) {
            LinkedTreeMap map = GsonUtil.fromJson(refreshDetailsInDb.getValue(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            channelDetails = GsonUtil.fromJson(result, Channel.class);
        }

        GenieResponse<Channel> response = null;
        if (channelDetails != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(channelDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(response.getError(), response.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

    private void initializeChannelDetails(String channelId) {
        String storedData = FileUtil.readFileFromClasspath(FrameworkConstants.ResourceFile.CHANNEL_DETAILS_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveChannelDetails(storedData);
        }
        refreshChannelDetails(channelId);
    }

    private void saveChannelDetails(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        Map result = ((LinkedTreeMap) map.get("result"));
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY);
            result.remove("ttl");
            for (Object key : result.keySet()) {
                NoSqlModel channelDetails = NoSqlModel.build(mAppContext.getDBSession(), (String) key, GsonUtil.toJson(result.get(key)));
                NoSqlModel channelDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), String.valueOf(key));
                if (channelDetailsInDb != null) {
                    channelDetails.update();
                } else {
                    channelDetails.save();
                }
            }
        }
    }

    private void refreshChannelDetails(final String channelId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChannelDetailsAPI channelAPI = new ChannelDetailsAPI(mAppContext, getCustomHeaders(authSession.getSessionData()), channelId);
                GenieResponse genieResponse = channelAPI.get();
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveChannelDetails(body);
                }
            }
        }).start();
    }

    @Override
    public GenieResponse<Framework> getFrameworkDetails(FrameworkDetailsRequest frameworkDetailsRequest) {
        String methodName = "getFrameworkDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(frameworkDetailsRequest));

        long expirationTime = getLongFromKeyValueStore(FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY);
        String frameworkId = frameworkDetailsRequest.getFrameworkId();
        if (expirationTime == 0) {
            initializeFrameworkDetails(frameworkId);
        } else if (hasExpired(expirationTime)) {
            refreshFrameworkDetails(frameworkId);
        }

        NoSqlModel refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), String.valueOf(FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY));

        Framework frameworkDetails = null;
        if (refreshDetailsInDb != null) {
            LinkedTreeMap map = GsonUtil.fromJson(refreshDetailsInDb.getValue(), LinkedTreeMap.class);
            String result = GsonUtil.toJson(map.get("result"));
            frameworkDetails = new Framework(result);
        }

        GenieResponse<Framework> response = null;
        if (frameworkDetails != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(frameworkDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(response.getError(), response.getMessage(), TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, response.getMessage());
        }
        return response;
    }

    private void initializeFrameworkDetails(String frameworkId) {
        String storedData = FileUtil.readFileFromClasspath(FrameworkConstants.ResourceFile.FRAMEWORK_DETAILS_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveFrameworkDetails(storedData);
        }
        refreshFrameworkDetails(frameworkId);
    }

    private void saveFrameworkDetails(String response) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        Map result = ((LinkedTreeMap) map.get("result"));
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY);
            result.remove("ttl");
            for (Object key : result.keySet()) {
                NoSqlModel frameworkDetails = NoSqlModel.build(mAppContext.getDBSession(), (String) key, GsonUtil.toJson(result.get(key)));
                NoSqlModel frameworkDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), String.valueOf(key));
                if (frameworkDetailsInDb != null) {
                    frameworkDetails.update();
                } else {
                    frameworkDetails.save();
                }
            }
        }
    }

    private boolean hasExpired(long expirationTime) {
        Long currentTime = DateUtil.getEpochTime();
        return currentTime > expirationTime;
    }

    private void refreshFrameworkDetails(final String frameworkId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FrameworkDetailsApi frameworkDetailsAPI = new FrameworkDetailsApi(mAppContext, getCustomHeaders(authSession.getSessionData()), frameworkId);
                GenieResponse genieResponse = frameworkDetailsAPI.get();
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveFrameworkDetails(body);
                }
            }
        }).start();
    }

    private void saveDataExpirationTime(Double ttl, String key) {
        if (ttl != null) {
            long ttlInMilliSeconds = (long) (ttl * DateUtil.MILLISECONDS_IN_AN_HOUR);
            Long currentTime = DateUtil.getEpochTime();
            long expiration_time = ttlInMilliSeconds + currentTime;

            mAppContext.getKeyValueStore().putLong(key, expiration_time);
        }
    }

}
