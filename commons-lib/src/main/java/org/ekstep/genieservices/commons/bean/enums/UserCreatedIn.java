package org.ekstep.genieservices.commons.bean.enums;

public enum UserCreatedIn {

    SERVER("server"), LOCAL("local");

    private String value;

    UserCreatedIn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
