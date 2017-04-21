package org.ekstep.genieservices.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author anil
 */
public class GsonUtil {

    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder().create();
        }
        return sGson;
    }

    public static <C> C toMap(String json, Class<C> classOfC) {
        return getGson().fromJson(json, classOfC);
    }

    public static String toString(Object map) {
        return getGson().toJson(map);
    }

    public static String toJson(Object json) {
        return getGson().toJson(json);
    }

}
