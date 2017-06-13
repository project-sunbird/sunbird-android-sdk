package org.ekstep.genieservices.commons.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anil
 */
public class MapUtil {

    public static <K, V> Map<K, V> asMap(K key, V value) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> toMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap>() {
        }.getType();
        Map<K, V> map = gson.fromJson(json, type);
        return map;
    }

    public static Map<String, Object> objectToMap(Object object) {
        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        String json = gson.toJson(object);

        return gson.fromJson(json, type);
    }

    public static List<HashMap> toListMap(String json) {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<HashMap>>() {
        }.getType();

        List<HashMap> mapList = gson.fromJson(json, type);
        return mapList;
    }

    public static String toString(Object map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

}
