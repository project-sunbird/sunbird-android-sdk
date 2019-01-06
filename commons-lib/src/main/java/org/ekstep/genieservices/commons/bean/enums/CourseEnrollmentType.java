package org.ekstep.genieservices.commons.bean.enums;


public enum CourseEnrollmentType {

    OPEN("open"), INVITE_ONLY("invite-only");

    private String value;

    CourseEnrollmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
