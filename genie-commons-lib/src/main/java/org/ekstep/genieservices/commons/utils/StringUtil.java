package org.ekstep.genieservices.commons.utils;

/**
 * Created by swayangjit on 18/4/17.
 */

public class StringUtil {

    /**
     * Returns {@code true} if the given string is null or is the empty string.
     *
     * @param string a string reference to check
     * @return {@code true} if the string is null or is the empty string
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }
}
