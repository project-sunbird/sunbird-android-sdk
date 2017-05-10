package org.ekstep.genieservices.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.ekstep.genieservices.commons.exception.InvalidDataException;

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

    public static <C> C fromJson(String json, Class<C> classOfC) {
        return getGson().fromJson(json, classOfC);
    }

    public static <T> T fromJson(String json, Class<T> type, String exceptionMessage){
        try {
            return getGson().fromJson(json, type);
        }catch (JsonSyntaxException ex){
            throw new InvalidDataException(exceptionMessage);
        }
    }

    public static String toJson(Object json) {
        return getGson().toJson(json);
    }

}
