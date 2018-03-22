package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.enums.LogLevel;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.utils.BuildConfigUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class BuildParams implements IParams {

    private static final int CONTENT_MIN_COMPATIBILITY_LEVEL = 1;
    private static final int CONTENT_MAX_COMPATIBILITY_LEVEL = 3;
    private static final int NETWORK_READ_TIMEOUT = 10;
    private static final int NETWORK_CONNECT_TIMEOUT = 10;

    private Context mContext;
    private String mPackageName;
    /**
     * Holds the actual values
     */
    private Map<String, Object> mValues;

    /**
     * Creates an empty set of values using the default initial size
     */
    public BuildParams(Context context, String packageName) {
        mContext = context;
        mPackageName = packageName;
        mValues = new HashMap<>();

        init(context, packageName);
    }

    private void init(Context context, String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            throw new IllegalArgumentException("packageName is mandatory, can not be null or empty.");
        }

        setParam(Key.VERSION_NAME);
        setParam(Key.APPLICATION_ID);
        setParam(Key.PRODUCER_ID);
        setParam(Key.PRODUCER_UNIQUE_ID);
        setParam(Key.CHANNEL_ID);
        setParam(Key.TELEMETRY_BASE_URL);
        setParam(Key.LANGUAGE_PLATFORM_BASE_URL);
        setParam(Key.TERMS_BASE_URL);
        setParam(Key.CONFIG_BASE_URL);
        setParam(Key.SEARCH_BASE_URL);
        setParam(Key.CONTENT_LISTING_BASE_URL);
        setParam(Key.CONTENT_BASE_URL);
        setParam(Key.USER_SERVICE_BASE_URL);
        setParam(Key.DATA_SERVICE_BASE_URL);
        setParam(Key.ORG_SERVICE_BASE_URL);
        setParam(Key.COURSE_SERVICE_BASE_URL);
        setParam(Key.PAGE_SERVICE_BASE_URL);
        setParam(Key.CHANNEL_SERVICE_BASE_URL);
        setParam(Key.FRAMEWORK_SERVICE_BASE_URL);
        setParam(Key.APIGATEWAY_BASE_URL);
        setParam(Key.API_USER);
        setParam(Key.API_PASS);
        setParam(Key.MOBILE_APP_SECRET);
        setParam(Key.MOBILE_APP_KEY);
        setParam(Key.MOBILE_APP_CONSUMER);
        setParam(Key.OAUTH_SERVICE_IMPLEMENTATION);
        setParam(ServiceConstants.Params.PLAYER_CONFIG);
        put(Key.LOG_LEVEL, LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(packageName, Key.LOG_LEVEL)).getLevel());

        initCompatibilityLevel(packageName);

        initNetworkParam(packageName);

        initProfilePath(context, packageName);
    }

    private void setParam(String key) {
        put(key, BuildConfigUtil.getBuildConfigValue(mPackageName, key));
    }

    private void setParam(IParams params, String key) {
        if (!StringUtil.isNullOrEmpty(params.getString(key))) {
            put(key, params.getString(key));
        } else {
            // Set default
            setParam(key);
        }
    }

    /**
     * Change or update the params in runtime.
     *
     * @param params {@link IParams}
     */
    public void setParams(IParams params) {
        if (params == null) {
            // Reload or reinitialize the default.
            init(mContext, mPackageName);
            return;
        }

        setParam(params, Key.VERSION_NAME);
        setParam(params, Key.APPLICATION_ID);
        setParam(params, Key.PRODUCER_ID);
        setParam(params, Key.PRODUCER_UNIQUE_ID);
        setParam(params, Key.CHANNEL_ID);
        setParam(params, Key.TELEMETRY_BASE_URL);
        setParam(params, Key.LANGUAGE_PLATFORM_BASE_URL);
        setParam(params, Key.TERMS_BASE_URL);
        setParam(params, Key.CONFIG_BASE_URL);
        setParam(params, Key.SEARCH_BASE_URL);
        setParam(params, Key.CONTENT_LISTING_BASE_URL);
        setParam(params, Key.CONTENT_BASE_URL);
        setParam(params, Key.USER_SERVICE_BASE_URL);
        setParam(params, Key.DATA_SERVICE_BASE_URL);
        setParam(params, Key.ORG_SERVICE_BASE_URL);
        setParam(params, Key.COURSE_SERVICE_BASE_URL);
        setParam(params, Key.PAGE_SERVICE_BASE_URL);
        setParam(params, Key.APIGATEWAY_BASE_URL);
        setParam(params, Key.API_USER);
        setParam(params, Key.API_PASS);
        setParam(params, Key.MOBILE_APP_SECRET);
        setParam(params, Key.MOBILE_APP_KEY);
        setParam(params, Key.MOBILE_APP_CONSUMER);
        setParam(params, Key.OAUTH_SERVICE_IMPLEMENTATION);

        if (!StringUtil.isNullOrEmpty(params.getString(Key.LOG_LEVEL))) {
            put(Key.LOG_LEVEL, LogLevel.getLogLevel(params.getString(Key.LOG_LEVEL)).getLevel());
        } else { // Set default.
            put(Key.LOG_LEVEL, LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(mPackageName, Key.LOG_LEVEL)).getLevel());
        }
    }

    private void initCompatibilityLevel(String packageName) {
        Object min = BuildConfigUtil.getBuildConfigValue(packageName, Key.MIN_COMPATIBILITY_LEVEL);
        int minCompatibilityLevel = 0;
        if (min != null) {
            minCompatibilityLevel = (int) min;
        }
        if (minCompatibilityLevel <= 0) {
            minCompatibilityLevel = CONTENT_MIN_COMPATIBILITY_LEVEL;
        }

        Object max = BuildConfigUtil.getBuildConfigValue(packageName, Key.MAX_COMPATIBILITY_LEVEL);
        int maxCompatibilityLevel = 0;
        if (max != null) {
            maxCompatibilityLevel = (int) max;
        }
        if (maxCompatibilityLevel <= 0) {
            maxCompatibilityLevel = CONTENT_MAX_COMPATIBILITY_LEVEL;
        }

        if (maxCompatibilityLevel < minCompatibilityLevel) {
            throw new IllegalStateException("MAX_COMPATIBILITY_LEVEL should not be less than MIN_COMPATIBILITY_LEVEL.");
        }

        put(Key.MIN_COMPATIBILITY_LEVEL, minCompatibilityLevel);
        put(Key.MAX_COMPATIBILITY_LEVEL, maxCompatibilityLevel);
    }

    private void initNetworkParam(String packageName) {
        Object connectTimeoutObj = BuildConfigUtil.getBuildConfigValue(packageName, Key.NETWORK_CONNECT_TIMEOUT);
        int connectTimeout = 0;
        if (connectTimeoutObj != null) {
            connectTimeout = (int) connectTimeoutObj;
        }
        if (connectTimeout <= 0) {
            connectTimeout = NETWORK_CONNECT_TIMEOUT;
        }

        Object readTimeoutObj = BuildConfigUtil.getBuildConfigValue(packageName, Key.NETWORK_READ_TIMEOUT);
        int readTimeout = 0;
        if (readTimeoutObj != null) {
            readTimeout = (int) readTimeoutObj;
        }
        if (readTimeout <= 0) {
            readTimeout = NETWORK_READ_TIMEOUT;
        }

        put(Key.NETWORK_CONNECT_TIMEOUT, connectTimeout);
        put(Key.NETWORK_READ_TIMEOUT, readTimeout);
    }

    private void initProfilePath(Context context, String packageName) {
        String profileConfigClass = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.PROFILE_CONFIG);
        if (profileConfigClass != null) {
            Class<?> classInstance = ReflectionUtil.getClass(profileConfigClass);
            if (classInstance != null) {
                IProfileConfig profileConfiguration = (IProfileConfig) ReflectionUtil.getInstance(classInstance);
                put(Key.PROFILE_PATH, profileConfiguration.getProfilePath(context));
            }
        }
    }

    @Override
    public void put(String key, Object value) {
        mValues.put(key, value);
    }

    @Override
    public String getString(String key) {
        return (String) mValues.get(key);
    }

    @Override
    public long getLong(String key) {
        return (long) mValues.get(key);
    }

    @Override
    public int getInt(String key) {
        return (int) mValues.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) mValues.get(key);
    }

    @Override
    public boolean contains(String key) {
        return mValues.containsKey(key);
    }

    @Override
    public void remove(String key) {
        mValues.remove(key);
    }

    @Override
    public void clear() {
        mValues.clear();
    }
}
