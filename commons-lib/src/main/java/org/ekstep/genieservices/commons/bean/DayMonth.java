package org.ekstep.genieservices.commons.bean;

/**
 * Created on 24/4/17.
 *  @author shriharsh
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
        return String.format("%d/%d/%d", new Object[]{Integer.valueOf(this.day), Integer.valueOf(this.month), Integer.valueOf(this.year)});
    }
}
