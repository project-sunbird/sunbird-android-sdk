package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.enums.LogLevel;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.utils.BuildConfigUtil;

import java.util.HashMap;

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

    /**
     * Holds the actual values
     */
    private HashMap<String, Object> mValues;

    /**
     * Creates an empty set of values using the default initial size
     */
    public BuildParams(Context context, String packageName) {
        mValues = new HashMap<>();

        init(context, packageName);
    }

    private void init(Context context, String packageName) {
        if (StringUtil.isNullOrEmpty(packageName)) {
            throw new IllegalArgumentException("packageName is mandatory, can not be null or empty.");
        }

        put(Key.VERSION_NAME, BuildConfigUtil.getBuildConfigValue(packageName, Key.VERSION_NAME));
        put(Key.APPLICATION_ID, BuildConfigUtil.getBuildConfigValue(packageName, Key.APPLICATION_ID));
        put(Key.PRODUCER_ID, BuildConfigUtil.getBuildConfigValue(packageName, Key.PRODUCER_ID));
        put(Key.CHANNEL_ID, BuildConfigUtil.getBuildConfigValue(packageName, Key.CHANNEL_ID));
        put(Key.APP_QUALIFIER, BuildConfigUtil.getBuildConfigValue(packageName, Key.APP_QUALIFIER));
        put(ServiceConstants.Params.PLAYER_CONFIG, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.PLAYER_CONFIG));
        put(Key.TELEMETRY_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.TELEMETRY_BASE_URL));
        put(Key.LANGUAGE_PLATFORM_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.LANGUAGE_PLATFORM_BASE_URL));
        put(Key.TERMS_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.TERMS_BASE_URL));
        put(Key.CONFIG_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.CONFIG_BASE_URL));
        put(Key.SEARCH_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.SEARCH_BASE_URL));
        put(Key.CONTENT_LISTING_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.CONTENT_LISTING_BASE_URL));
        put(Key.CONTENT_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.CONTENT_BASE_URL));
        put(Key.APIGATEWAY_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, Key.APIGATEWAY_BASE_URL));
        put(Key.API_USER, BuildConfigUtil.getBuildConfigValue(packageName, Key.API_USER));
        put(Key.API_PASS, BuildConfigUtil.getBuildConfigValue(packageName, Key.API_PASS));
        put(Key.MOBILE_APP_SECRET, BuildConfigUtil.getBuildConfigValue(packageName, Key.MOBILE_APP_SECRET));
        put(Key.MOBILE_APP_KEY, BuildConfigUtil.getBuildConfigValue(packageName, Key.MOBILE_APP_KEY));
        put(Key.MOBILE_APP_CONSUMER, BuildConfigUtil.getBuildConfigValue(packageName, Key.MOBILE_APP_CONSUMER));
        put(Key.LOG_LEVEL, LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(packageName, Key.LOG_LEVEL)).getLevel());

        initCompatibilityLevel(packageName);

        initNetworkParam(packageName);

        String profileConfigClass = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.PROFILE_CONFIG);
        if (profileConfigClass != null) {
            Class<?> classInstance = ReflectionUtil.getClass(profileConfigClass);
            if (classInstance != null) {
                IProfileConfig profileConfiguration = (IProfileConfig) ReflectionUtil.getInstance(classInstance);
                put(Key.PROFILE_PATH, profileConfiguration.getProfilePath(context));
            }
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
