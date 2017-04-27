package org.ekstep.genieservices.utils;

import org.ekstep.genieservices.commons.utils.ReflectionUtils;

/**
 * Created on 4/18/2017.
 *
 * @author anil
 */
public class BuildConfigUtil {

    public static Class<?> getBuildConfigClass(String packageName) {
        return ReflectionUtils.getClass(packageName + ".BuildConfig");
    }

    public static <T> T getBuildConfigValue(String packageName, String property) {
        Object value = ReflectionUtils.getStaticFieldValue(getBuildConfigClass(packageName), property);
        return (T) value;
    }
}
