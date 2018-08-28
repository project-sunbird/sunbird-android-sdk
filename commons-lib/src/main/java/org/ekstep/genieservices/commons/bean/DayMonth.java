package org.ekstep.genieservices.commons.bean;

import java.util.Locale;

/**
 * This class holds the day, month and year data.
 */
public class DayMonth {
    private int day;
    private int month;
    private int year;

    public DayMonth(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public boolean isParsable() {
        return this.day != -1 || this.month != -1;
    }

    public String toString() {
        return String.format(Locale.US, "%d/%d/%d", this.day, this.month, this.year);
    }
}
