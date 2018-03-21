package org.ekstep.genieservices.commons.bean.enums;

/**
 * Enum to hold user profile type.
 * <p>
 * "student"
 * "teacher"
 */

public enum ProfileType {

    STUDENT("student"), TEACHER("teacher");

    private String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
