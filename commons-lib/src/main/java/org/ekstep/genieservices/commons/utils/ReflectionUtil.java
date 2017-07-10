package org.ekstep.genieservices.commons.utils;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class ReflectionUtil {

    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
        try {
            return clazz.getField(fieldName).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getInstance(Class<T> classInstance) {
        T instance = null;
        if (classInstance != null) {
            try {
                instance = classInstance.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instance;
    }
}
