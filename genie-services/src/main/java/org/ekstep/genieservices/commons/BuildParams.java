package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.enums.LogLevel;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.utils.BuildConfigUtil;

import java.util.HashMap;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class BuildParams implements IParams {

    /**
     * Holds the actual values
     */
    private HashMap<String, Object> mValues;

    /**
     * Creates an empty set of values using the default initial size
     */
    public BuildParams(Context context, String packageName) {
        // Choosing a default size of 16 based on analysis of typical
        // consumption by applications.
        mValues = new HashMap<>(16);

        init(context, packageName);
    }

    private void init(Context context, String packageName) {
        put(ServiceConstants.Params.VERSION_NAME, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.VERSION_NAME));
        put(ServiceConstants.Params.APP_QUALIFIER, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.APP_QUALIFIER));
        put(ServiceConstants.Params.TELEMETRY_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.TELEMETRY_BASE_URL));
        put(ServiceConstants.Params.LANGUAGE_PLATFORM_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.LANGUAGE_PLATFORM_BASE_URL));
        put(ServiceConstants.Params.LEARNING_PLATFORM_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.LEARNING_PLATFORM_BASE_URL));
        put(ServiceConstants.Params.CONFIG_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.CONFIG_BASE_URL));
        put(ServiceConstants.Params.SEARCH_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.SEARCH_BASE_URL));
        put(ServiceConstants.Params.CONTENT_LISTING_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.CONTENT_LISTING_BASE_URL));
        put(ServiceConstants.Params.ANALYTICS_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.ANALYTICS_BASE_URL));
        put(ServiceConstants.Params.API_USER, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.API_USER));
        put(ServiceConstants.Params.API_PASS, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.API_PASS));
        put(ServiceConstants.Params.GID, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.GID));
        put(ServiceConstants.Params.MOBILE_APP_SECRET, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.MOBILE_APP_SECRET));
        put(ServiceConstants.Params.MOBILE_APP_KEY, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.MOBILE_APP_KEY));
        put(ServiceConstants.Params.MOBILE_APP_CONSUMER, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.MOBILE_APP_CONSUMER));
        put(ServiceConstants.Params.LOG_LEVEL, LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.LOG_LEVEL)).getLevel());

        initCompatibilityLevel(packageName);

        String profileConfigClass = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.PROFILE_CONFIG);
        if (profileConfigClass != null) {
            Class<?> classInstance = ReflectionUtil.getClass(profileConfigClass);
            if (classInstance != null) {
                IProfileConfig profileConfiguration = (IProfileConfig) ReflectionUtil.getInstance(classInstance);
                put(ServiceConstants.Params.PROFILE_PATH, profileConfiguration.getProfilePath(context));
            }
        }
    }

    private void initCompatibilityLevel(String packageName) {
        Object min = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.MIN_COMPATIBILITY_LEVEL);
        int minCompatibilityLevel = 0;
        if (min != null) {
            minCompatibilityLevel = (int) min;
        }
        if (minCompatibilityLevel <= 0) {
            minCompatibilityLevel = 1;
        }

        Object max = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.MAX_COMPATIBILITY_LEVEL);
        int maxCompatibilityLevel = 0;
        if (max != null) {
            maxCompatibilityLevel = (int) max;
        }
        if (maxCompatibilityLevel <= 0) {
            maxCompatibilityLevel = 3;
        }

        if (maxCompatibilityLevel < minCompatibilityLevel) {
            throw new IllegalStateException("MAX_COMPATIBILITY_LEVEL should not be less than MIN_COMPATIBILITY_LEVEL.");
        }

        put(ServiceConstants.Params.MIN_COMPATIBILITY_LEVEL, minCompatibilityLevel);
        put(ServiceConstants.Params.MAX_COMPATIBILITY_LEVEL, maxCompatibilityLevel);
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
