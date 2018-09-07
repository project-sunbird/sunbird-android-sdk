package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IFrameworkService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.Channel;
import org.ekstep.genieservices.commons.bean.ChannelDetailsRequest;
import org.ekstep.genieservices.commons.bean.Framework;
import org.ekstep.genieservices.commons.bean.FrameworkDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.config.network.ChannelDetailsAPI;
import org.ekstep.genieservices.config.network.FrameworkDetailsAPI;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of the interface {@link IFrameworkService}
 *
 * @author indraja
 */
public class FrameworkServiceImpl extends BaseService implements IFrameworkService {

    private static final String TAG = FrameworkServiceImpl.class.getSimpleName();
    private static final String DB_KEY_CHANNEL_DETAILS = "channel_details_key-";
    private static final String DB_KEY_FRAMEWORK_DETAILS = "framework_details_key-";
    private static final Double DEFAULT_TTL = 1d;   // In hours

    public FrameworkServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Channel> getChannelDetails(final ChannelDetailsRequest channelDetailsRequest) {
        String methodName = "getChannelDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(channelDetailsRequest));
        params.put("logLevel", "2");

        GenieResponse<Channel> response;

        String key = DB_KEY_CHANNEL_DETAILS + channelDetailsRequest.getChannelId();
        String expirationKey = FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY + "-" + channelDetailsRequest.getChannelId();
        long expirationTime = getLongFromKeyValueStore(expirationKey);

        NoSqlModel channelDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);

        if (channelDetailsInDb == null) {
            String responseBody = null;
            if (!StringUtil.isNullOrEmpty(channelDetailsRequest.getFilePath())) {
                responseBody = FileUtil.readFileFromClasspath(channelDetailsRequest.getFilePath());
            }

            GenieResponse channelDetailsAPIResponse;
            if (StringUtil.isNullOrEmpty(responseBody)) {
                channelDetailsAPIResponse = getChannelDetailsFromServer(channelDetailsRequest.getChannelId());
                if (channelDetailsAPIResponse.getStatus()) {
                    responseBody = channelDetailsAPIResponse.getResult().toString();
                } else {
                    List<String> errorMessages = channelDetailsAPIResponse.getErrorMessages();
                    String errorMessage = null;
                    if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                        errorMessage = errorMessages.get(0);
                    }
                    response = GenieResponseBuilder.getErrorResponse(channelDetailsAPIResponse.getError(), errorMessage, TAG);
                    TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
                    return response;
                }
            }

            saveChannelDetails(responseBody, channelDetailsRequest.getChannelId());

        } else if (hasExpired(expirationTime)) {
            refreshChannelDetails(channelDetailsRequest.getChannelId());
        }

        channelDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
        Channel channelDetails = null;
        if (channelDetailsInDb != null) {
            LinkedTreeMap map = GsonUtil.fromJson(channelDetailsInDb.getValue(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String channel = GsonUtil.toJson(result.get("channel"));
            channelDetails = GsonUtil.fromJson(channel, Channel.class);
        }

        if (channelDetails != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(channelDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_CHANNEL_DETAILS_FOUND,
                    ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CHANNEL_DETAILS, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params,
                    ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CHANNEL_DETAILS);
        }
        return response;
    }

    private GenieResponse getChannelDetailsFromServer(String channelId) {
        ChannelDetailsAPI channelAPI = new ChannelDetailsAPI(mAppContext, channelId);
        return channelAPI.get();
    }

    private void saveChannelDetails(String response, String channelId) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        LinkedTreeMap result = (LinkedTreeMap) map.get("result");
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            String expirationKey = FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY + "-" + channelId;
            saveDataExpirationTime(ttl, expirationKey);

            String key = DB_KEY_CHANNEL_DETAILS + channelId;
            NoSqlModel channelDetails = NoSqlModel.build(mAppContext.getDBSession(), key, response);
            NoSqlModel channelDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
            if (channelDetailsInDb != null) {
                channelDetails.update();
            } else {
                channelDetails.save();
            }
        }
    }

    private void refreshChannelDetails(final String channelId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChannelDetailsAPI channelAPI = new ChannelDetailsAPI(mAppContext, channelId);
                GenieResponse genieResponse = channelAPI.get();
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveChannelDetails(body, channelId);
                }
            }
        }).start();
    }

    @Override
    public GenieResponse<Framework> getFrameworkDetails(FrameworkDetailsRequest frameworkDetailsRequest) {
        String methodName = "getFrameworkDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(frameworkDetailsRequest));
        params.put("logLevel", "2");

        GenieResponse<Framework> response;
        String responseBody = null;

        String key = DB_KEY_FRAMEWORK_DETAILS + frameworkDetailsRequest.getFrameworkId();
        String expirationKey = FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY + "-" + frameworkDetailsRequest.getFrameworkId();
        long expirationTime = getLongFromKeyValueStore(expirationKey);

        NoSqlModel frameworkDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);

        //no expiration time in shared preferences
        if (frameworkDetailsInDb == null) {
            if (!StringUtil.isNullOrEmpty(frameworkDetailsRequest.getFilePath())) {
                responseBody = FileUtil.readFileFromClasspath(frameworkDetailsRequest.getFilePath());
            }

            GenieResponse frameworkDetailsAPIResponse;
            if (StringUtil.isNullOrEmpty(responseBody)) {
                frameworkDetailsAPIResponse = getFrameworkDetailsFromServer(frameworkDetailsRequest.getFrameworkId());
                if (frameworkDetailsAPIResponse.getStatus()) {
                    String responseBodyFromNetwork = frameworkDetailsAPIResponse.getResult().toString();
                    responseBody = responseBodyFromNetwork;
                    saveFrameworkExpirationTime(responseBodyFromNetwork);
                } else {
                    List<String> errorMessages = frameworkDetailsAPIResponse.getErrorMessages();
                    String errorMessage = null;
                    if (!CollectionUtil.isNullOrEmpty(errorMessages)) {
                        errorMessage = errorMessages.get(0);
                    }
                    response = GenieResponseBuilder.getErrorResponse(frameworkDetailsAPIResponse.getError(), errorMessage, TAG);
                    TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
                    return response;
                }
            }
        } else {
            //get from db including default
            responseBody = frameworkDetailsInDb.getValue();

            //make a silent call to update in db only if network in available
            // and the ttl is expired
            if (mAppContext.getConnectionInfo().isConnected() && hasExpired(expirationTime)) {
                GenieResponse frameworkDetailsAPIResponse = getFrameworkDetailsFromServer(frameworkDetailsRequest.getFrameworkId());
                if (frameworkDetailsAPIResponse.getStatus()) {
                    String responseBodyFromNetwork = frameworkDetailsAPIResponse.getResult().toString();
                    responseBody = responseBodyFromNetwork;
                    saveFrameworkExpirationTime(responseBodyFromNetwork);
                }
            }
        }

        if (!StringUtil.isNullOrEmpty(responseBody)) {
            Framework frameworkDetails = new Framework(responseBody);
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(frameworkDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_FRAMEWORK_DETAILS_FOUND,
                    ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FRAMEWORK_DETAILS, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params,
                    ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FRAMEWORK_DETAILS);
        }
        return response;
    }

    private boolean hasExpired(long expirationTime) {
        Long currentTime = DateUtil.getEpochTime();
        return currentTime > expirationTime;
    }

    private GenieResponse getFrameworkDetailsFromServer(String frameworkId) {
        FrameworkDetailsAPI frameworkDetailsAPI = new FrameworkDetailsAPI(mAppContext, frameworkId);
        return frameworkDetailsAPI.get();
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

    @Override
    public GenieResponse<Void> persistFrameworkDetails(String responseBody) {
        LinkedTreeMap map = GsonUtil.fromJson(responseBody, LinkedTreeMap.class);
        Map resultMap = ((LinkedTreeMap) map.get("result"));
        if (resultMap != null) {
            LinkedTreeMap frameworkMap = (LinkedTreeMap) resultMap.get("framework");
            String frameworkId = (String) frameworkMap.get("identifier");
            String key = DB_KEY_FRAMEWORK_DETAILS + frameworkId;

            NoSqlModel frameworkDetails = NoSqlModel.build(mAppContext.getDBSession(), key, responseBody);
            NoSqlModel frameworkDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
            if (frameworkDetailsInDb != null) {
                frameworkDetails.update();
            } else {
                frameworkDetails.save();
            }
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    private void saveFrameworkExpirationTime(String responseBody) {
        LinkedTreeMap map = GsonUtil.fromJson(responseBody, LinkedTreeMap.class);
        Map resultMap = ((LinkedTreeMap) map.get("result"));
        if (resultMap != null) {
            Double ttl = (Double) resultMap.get("ttl");
            LinkedTreeMap frameworkMap = (LinkedTreeMap) resultMap.get("framework");
            String frameworkId = (String) frameworkMap.get("identifier");

            String expirationKey = FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY + "-" + frameworkId;
            saveDataExpirationTime(ttl, expirationKey);
        }
    }

}
