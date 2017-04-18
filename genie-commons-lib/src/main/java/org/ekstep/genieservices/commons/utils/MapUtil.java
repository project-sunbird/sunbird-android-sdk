package org.ekstep.genieservices.commons.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anil
 */
public class MapUtil {

    public static <K, V> Map<K, V> toMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap>() {
        }.getType();
        Map<K, V> map = gson.fromJson(json, type);
        return map;
    }

}
