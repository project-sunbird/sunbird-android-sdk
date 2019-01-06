package org.ekstep.genieservices.commons.bean.enums;


public enum CourseBatchStatus {

    NOT_STARTED("0"), IN_PROGRESS("1"), COMPLETED("2");

    private String value;

    CourseBatchStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
