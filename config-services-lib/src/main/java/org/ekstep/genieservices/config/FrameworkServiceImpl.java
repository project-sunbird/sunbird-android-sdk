package org.ekstep.genieservices.config;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IFrameworkService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.IParams;
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

    public FrameworkServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<Channel> getChannelDetails(final ChannelDetailsRequest channelDetailsRequest) {
        String methodName = "getChannelDetails@FrameworkServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("request", GsonUtil.toJson(channelDetailsRequest));
        params.put("logLevel", "2");

        String channelId = channelDetailsRequest.getChannelId();
        long expirationTime = getLongFromKeyValueStore(FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY);
        NoSqlModel refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), DB_KEY_CHANNEL_DETAILS + channelId);
        if (expirationTime == 0 && refreshDetailsInDb == null) {
            initializeChannelDetails(channelId);
        } else if (hasExpired(expirationTime)) {
            refreshChannelDetails(channelId);
        }

        refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), DB_KEY_CHANNEL_DETAILS + channelId);
        Channel channelDetails = null;
        if (refreshDetailsInDb != null) {
            LinkedTreeMap map = GsonUtil.fromJson(refreshDetailsInDb.getValue(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String channel = GsonUtil.toJson(result.get("channel"));
            channelDetails = GsonUtil.fromJson(channel, Channel.class);
        }

        GenieResponse<Channel> response;
        if (channelDetails != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(channelDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_CHANNEL_DETAILS_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CHANNEL_DETAILS, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_CHANNEL_DETAILS);
        }
        return response;
    }

    private void initializeChannelDetails(String channelId) {
        String storedData = FileUtil.readFileFromClasspath(FrameworkConstants.ResourceFile.CHANNEL_DETAILS_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveChannelDetails(storedData, channelId);
        }

        refreshChannelDetails(channelId);
    }

    private void saveChannelDetails(String response, String channelId) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        LinkedTreeMap result = (LinkedTreeMap) map.get("result");
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            saveDataExpirationTime(ttl, FrameworkConstants.PreferenceKey.CHANNEL_DETAILS_API_EXPIRATION_KEY);
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

        String frameworkId;
        if (frameworkDetailsRequest.isDefaultFrameworkDetails()) {
            frameworkId = getDefaultFrameworkDetails();
        } else {
            frameworkId = frameworkDetailsRequest.getFrameworkId();
        }

        String expirationKey = FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY + "-" + frameworkId;
        long expirationTime = getLongFromKeyValueStore(expirationKey);
        NoSqlModel refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), DB_KEY_FRAMEWORK_DETAILS + frameworkId);

        if (expirationTime == 0 && refreshDetailsInDb == null && frameworkDetailsRequest.isDefaultFrameworkDetails()) {
            initializeFrameworkDetails(frameworkId);
        } else if (hasExpired(expirationTime)) {
            GenieResponse frameworkDetailsAPIResponse = getFrameworkDetailsFromServer(frameworkId);
            if (frameworkDetailsAPIResponse.getStatus()) {
                String body = frameworkDetailsAPIResponse.getResult().toString();
                saveFrameworkDetails(body, frameworkId);
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

        refreshDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), DB_KEY_FRAMEWORK_DETAILS + frameworkId);
        Framework frameworkDetails = null;
        if (refreshDetailsInDb != null) {
            LinkedTreeMap map = GsonUtil.fromJson(refreshDetailsInDb.getValue(), LinkedTreeMap.class);
            LinkedTreeMap result = (LinkedTreeMap) map.get("result");
            String framework = GsonUtil.toJson(result.get("framework"));
            frameworkDetails = new Framework(framework);
        }

        if (frameworkDetails != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            response.setResult(frameworkDetails);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.NO_FRAMEWORK_DETAILS_FOUND, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FRAMEWORK_DETAILS, TAG);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_FRAMEWORK_DETAILS);

        }

        return response;
    }

    /**
     * get default framework details
     *
     * @return
     */
    private String getDefaultFrameworkDetails() {
        ChannelDetailsRequest channelDetailsRequest = new ChannelDetailsRequest.Builder()
                .forChannel(mAppContext.getParams().getString(IParams.Key.CHANNEL_ID))
                .build();
        GenieResponse<Channel> channelDetailsResponse = getChannelDetails(channelDetailsRequest);
        Channel channelDetails = channelDetailsResponse.getResult();
        return channelDetails.getDefaultFramework();
    }

    private void initializeFrameworkDetails(String frameworkId) {
        String storedData = FileUtil.readFileFromClasspath(FrameworkConstants.ResourceFile.FRAMEWORK_DETAILS_JSON_FILE);
        if (!StringUtil.isNullOrEmpty(storedData)) {
            saveFrameworkDetails(storedData, frameworkId);
        }
        refreshFrameworkDetails(frameworkId);
    }

    private void saveFrameworkDetails(String response, String frameworkId) {
        LinkedTreeMap map = GsonUtil.fromJson(response, LinkedTreeMap.class);
        Map result = ((LinkedTreeMap) map.get("result"));
        if (result != null) {
            Double ttl = (Double) result.get("ttl");
            String expirationKey = FrameworkConstants.PreferenceKey.FRAMEWORK_DETAILS_API_EXPIRATION_KEY + "-" + frameworkId;
            saveDataExpirationTime(ttl, expirationKey);
            String key = DB_KEY_FRAMEWORK_DETAILS + frameworkId;
            NoSqlModel frameworkDetails = NoSqlModel.build(mAppContext.getDBSession(), key, response);
            NoSqlModel frameworkDetailsInDb = NoSqlModel.findByKey(mAppContext.getDBSession(), key);
            if (frameworkDetailsInDb != null) {
                frameworkDetails.update();
            } else {
                frameworkDetails.save();
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
                GenieResponse genieResponse = getFrameworkDetailsFromServer(frameworkId);
                if (genieResponse.getStatus()) {
                    String body = genieResponse.getResult().toString();
                    saveFrameworkDetails(body, frameworkId);
                }
            }
        }).start();
    }

    private GenieResponse getFrameworkDetailsFromServer(String frameworkId) {
        FrameworkDetailsAPI frameworkDetailsAPI = new FrameworkDetailsAPI(mAppContext, frameworkId);
        return frameworkDetailsAPI.get();
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
