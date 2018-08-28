package org.ekstep.genieservices.commons.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 18/4/17.
 *
 * @author swayangjit
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

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens an array objects to be joined. Strings will be formed from
     *               the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens an array objects to be joined. Strings will be formed from
     *               the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * This method gets you the first part of the string that is divided after last index of "/"
     *
     * @param contentFolderName
     * @return
     */
    public static String getFirstPartOfThePathNameOnLastDelimiter(String contentFolderName) {
        int lastIndexOfDelimiter = contentFolderName.lastIndexOf("/");

        if (lastIndexOfDelimiter > 0 && lastIndexOfDelimiter < contentFolderName.length()) {
            return contentFolderName.substring(0, lastIndexOfDelimiter);
        }

        return null;
    }

    public static List<String> getStringWithQuoteList(List<String> collection) {
        ArrayList<String> stringWithCommaList = new ArrayList<>();

        if (!CollectionUtil.isNullOrEmpty(collection)) {
            for (String s : collection) {
                stringWithCommaList.add("'" + s + "'");

            }
        }

        return stringWithCommaList;
    }
}
