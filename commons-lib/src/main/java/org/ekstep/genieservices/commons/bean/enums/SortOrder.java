package org.ekstep.genieservices.commons.bean.enums;

/**
 * Enum to hold sort direction
 */
public enum SortOrder {

    ASC("asc"), DESC("desc");

    private String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
