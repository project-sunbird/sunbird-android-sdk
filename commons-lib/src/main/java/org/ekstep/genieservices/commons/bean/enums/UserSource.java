package org.ekstep.genieservices.commons.bean.enums;

public enum UserSource {

    SERVER("server"), LOCAL("local");

    private String value;

    UserSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
