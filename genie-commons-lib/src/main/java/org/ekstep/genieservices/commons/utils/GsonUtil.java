package org.ekstep.genieservices.commons.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author anil
 */
public class GsonUtil {

    public static <K, V> Map<K, V> toMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map>() {
        }.getType();
        Map<K, V> map = gson.fromJson(json, type);
        return map;
    }

    public static <C> C fromJson(String json, Class<C> classOfC) {
       return new Gson().fromJson(json, classOfC);
    }

    public static String toJson(Object json) {
        return new Gson().toJson(json);
    }
}
