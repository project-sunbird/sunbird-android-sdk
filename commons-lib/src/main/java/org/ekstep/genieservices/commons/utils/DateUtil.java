package org.ekstep.genieservices.commons.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final int MILLISECONDS_IN_AN_HOUR = 3600000;
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";
    private static final String DATETIME_FORMAT_WITHOUTTIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_AM_PM_FORMAT = "dd/MM/yyyy, hh:mma";


    public static String getCurrentTimestamp() {
        return format(getEpochTime(), DATETIME_FORMAT);
    }

    public static Long getEpochTime() {
        return System.currentTimeMillis();
    }

    public static DateTime parse(String dateInString, String format) {
        return DateTimeFormat.forPattern(format).parseDateTime(dateInString);
    }

    public static Long getTime(String date) {
        DateTime dateTime = parse(date, DATETIME_FORMAT_WITHOUTTIMEZONE);
        return dateTime.getMillis();
    }

    public static String format(Date date, String format) {
        return DateTimeFormat.forPattern(format).print(date.getTime());
    }

    public static String format(long dateTime,String format) {
        return format(new Date(dateTime),format);
    }

    public static Integer elapsedTimeTillNow(String dateInString) {
        DateTime parseDateTime = parse(dateInString, DATETIME_FORMAT);
        DateTime now = DateTime.now();
        Seconds seconds = Seconds.secondsBetween(parseDateTime, now);
        return seconds.getSeconds();
    }

    public static Long getMidnightEpochTime(int offsetDays) {
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = today.plusDays(offsetDays + 1).withTimeAtStartOfDay();
        return tomorrow.getMillis();
    }

    public static Long getTodayMidnightEpochTime() {
        return getMidnightEpochTime(0);
    }

    public static boolean isTodayWithin(String startDate, String endDate) {
        String today = format(new Date().getTime(), DATE_FORMAT);
        return isDateBetween(startDate, endDate, today);
    }

    public static boolean isDateBetween(String startDate, String endDate, String dateToCompare) {
        LocalDate date = parse(dateToCompare, DATE_FORMAT).toLocalDate();
        LocalDate startDateTime = (startDate == null) ? date : parse(startDate, DATE_FORMAT).toLocalDate();
        LocalDate endDateTime = (endDate == null) ? date : parse(endDate, DATE_FORMAT).toLocalDate();
        return date.isAfter(startDateTime) && date.isBefore(endDateTime);
    }


}
