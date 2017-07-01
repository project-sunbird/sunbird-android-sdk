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
        // Choosing a default size of 8 based on analysis of typical
        // consumption by applications.
        mValues = new HashMap<>(8);

        mValues.put(ServiceConstants.Params.VERSION_NAME, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.VERSION_NAME));
        mValues.put(ServiceConstants.Params.APP_QUALIFIER, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.APP_QUALIFIER));
        mValues.put(ServiceConstants.Params.API_BASE_URL, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.API_BASE_URL));
        mValues.put(ServiceConstants.Params.API_USER, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.API_USER));
        mValues.put(ServiceConstants.Params.API_PASS, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.API_PASS));
        mValues.put(ServiceConstants.Params.GID, BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.GID));
        mValues.put(ServiceConstants.Params.LOGLEVEL, LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.LOGLEVEL)).getLevel());

        String profileConfigClass = BuildConfigUtil.getBuildConfigValue(packageName, ServiceConstants.Params.PROFILE_CONFIG);
        if (profileConfigClass != null) {
            Class<?> classInstance = ReflectionUtil.getClass(profileConfigClass);
            if (classInstance != null) {
                IProfileConfig profileConfiguration = (IProfileConfig) ReflectionUtil.getInstance(classInstance);
                mValues.put(ServiceConstants.Params.PROFILE_PATH, profileConfiguration.getProfilePath(context));
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
