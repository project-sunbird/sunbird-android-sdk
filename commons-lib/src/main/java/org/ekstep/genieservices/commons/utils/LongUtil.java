package org.ekstep.genieservices.commons.utils;

/**
 * Created by swayangjit on 12/6/17.
 */

public class LongUtil {

    public static long tryParseToLong(String toParse, long defaultValue) {
        try {
            return Long.parseLong(String.valueOf(toParse));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
