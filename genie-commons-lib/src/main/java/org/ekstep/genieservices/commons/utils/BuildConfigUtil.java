package org.ekstep.genieservices.commons.utils;

/**
 * Created on 4/18/2017.
 *
 * @author anil
 */
public class BuildConfigUtil {

    public static final String VERSION_CODE = "VERSION_CODE";
    public static final String VERSION_NAME = "VERSION_NAME";

    public static final String API_BASE_URL = "API_BASE_URL";
    public static final String ANALYTICS_BASE_URL = "ANALYTICS_BASE_URL";
    public static final String TAXONOMY_BASE_URL = "TAXONOMY_BASE_URL";
    public static final String API_PASS = "API_PASS";
    public static final String API_USER = "API_USER";

    private static final String TAG = BuildConfigUtil.class.getSimpleName();

    public static Object getBuildConfigValue(String packageName, String fieldName) {

        Class<?> buildConfigClass = null;
        try {
            buildConfigClass = loadBuildConfigClass(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            throw new IllegalArgumentException("appPackage should be same as defined in Manifest.xml");
        }

        try {
            return getStaticFieldValue(buildConfigClass, fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class<?> loadBuildConfigClass(String packageName) throws ClassNotFoundException {
        return loadClass(packageName + ".BuildConfig");
    }

    private static Class<?> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private static Object getStaticFieldValue(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getField(fieldName).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
