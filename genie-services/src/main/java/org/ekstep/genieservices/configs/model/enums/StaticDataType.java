package org.ekstep.genieservices.configs.model.enums;

/**
 * StaticDataTypes
 *
 */
public enum StaticDataType {
    SUBJECT("subject"),
    MEDIUM("medium"),
    BOARD("board"),
    AGE("age"),
    AGEGROUP("ageGroup"),
    GRADELEVEL("gradeLevel");

    private final String value;

    StaticDataType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
