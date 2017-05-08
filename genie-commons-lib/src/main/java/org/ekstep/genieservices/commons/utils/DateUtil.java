package org.ekstep.genieservices.commons.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final int MILLISECONDS_IN_AN_HOUR = 3600000;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";

    public static String getCurrentTimestamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT).withLocale(Locale.US);
        DateTime dateTime = new DateTime();
        return dateTime.toString(dateTimeFormatter);
    }

    public static Long getEpochTime() {
        return System.currentTimeMillis();
    }

    public static String getEpochTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    public static long convertLocalTimeMillis(String dateTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = dateFormat.parse(dateTime);
        return date.getTime();
    }

    public static String convertLocalTimeMillisToString(long dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date(dateTime);
        String timeRequired = sdf.format(date);
        return timeRequired;
    }

    public static Long dateToEpoch(String dateInString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            sdf.parse(dateInString).getTime();
            return sdf.parse(dateInString).getTime();
        } catch (Exception ex) {
            return getEpochTime();
        }
    }

    public static Long dateToEpoch(String dateInString, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            sdf.parse(dateInString).getTime();
            return sdf.parse(dateInString).getTime();
        } catch (Exception ex) {
            return getEpochTime();
        }
    }

    public static DateTime parseDateTime(String dateInString) {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ").parseDateTime(dateInString);
    }

}
