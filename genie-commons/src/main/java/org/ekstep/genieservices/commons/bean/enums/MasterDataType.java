package org.ekstep.genieservices.commons.bean.enums;

/**
 * MasterDataTypes
 *
 */
public enum MasterDataType {
    SUBJECT("subject"),
    MEDIUM("medium"),
    BOARD("board"),
    AGE("age"),
    AGEGROUP("ageGroup"),
    GRADELEVEL("gradeLevel");

    private final String value;

    MasterDataType(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
