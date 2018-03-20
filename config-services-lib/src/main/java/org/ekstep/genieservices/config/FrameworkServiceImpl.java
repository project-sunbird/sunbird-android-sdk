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

        // TODO 15/03/18 : add a thread if we are saving db for offline
        ChannelDetailsAPI channelAPI = new ChannelDetailsAPI(mAppContext, getCustomHeaders(authSession.getSessionData()), channelDetailsRequest.getChannelId());
        GenieResponse genieResponse = channelAPI.get();

        LinkedTreeMap map = GsonUtil.fromJson(genieResponse.getResult().toString(), LinkedTreeMap.class);
        String result = GsonUtil.toJson(map.get("result"));
        Channel channel = GsonUtil.fromJson(result, Channel.class);

        GenieResponse<Channel> response;
        if (channel != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(channel);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(genieResponse.getError(), genieResponse.getMessage(), TAG);

            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, genieResponse.getMessage());
        }
        return response;
    }

    @Override
    public GenieResponse<Framework> getFrameworkDetails(FrameworkDetailsRequest frameworkDetailsRequest) {
        String methodName = "getFrameworkDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(frameworkDetailsRequest));

        long expirationTime = getLongFromKeyValueStore(FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY);
        if (expirationTime == 0) {
            initializeFrameworkDetails(frameworkDetailsRequest.getFrameworkId());
        } else if (hasExpired(expirationTime)) {
            refreshFrameworkDetails(frameworkDetailsRequest.getFrameworkId());
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
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_MASTER_DATA);
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
