package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created by swayangjit on 22/5/17.
 */

public enum LogLevel {

    TRACE("TRACE", 1), DEBUG("DEBUG", 2), INFO("INFO", 3), ERROR("ERROR", 4);

    private String value;
    private int level;

    LogLevel(String value, int level) {
        this.value = value;
        this.level = level;
    }

    public static LogLevel getLogLevel(Object value) {
        if (value != null) {
            for (LogLevel logLevel : LogLevel.values()) {
                if (logLevel.value.equalsIgnoreCase(value.toString())) {
                    return logLevel;
                }
            }
        }

        return LogLevel.INFO;
    }

    public String getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

}
