package org.ekstep.genieservices.commons.bean.enums;

/**
 * Enum to hold user profile fields.
 * <p>
 * "address"
 * "education"
 * "jobProfile"
 * "dob"
 * "grade"
 * "gender"
 * "profileSummary"
 * "lastName"
 * "subject"
 * "location"
 * "language"
 * "avatar"
 */
public enum UserProfileFields {

    COMPLETENESS("completeness"), MISSING_FIELDS("missingFields"),
    LAST_LOGIN_TIME("lastLoginTime"), TOPICS("topics");

    private String value;

    UserProfileFields(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
