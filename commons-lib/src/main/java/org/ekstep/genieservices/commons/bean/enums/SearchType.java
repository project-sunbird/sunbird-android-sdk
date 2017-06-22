package org.ekstep.genieservices.commons.bean.enums;

/**
 * Enum to hold search types
 */
public enum SearchType {

    SEARCH("search"), FILTER("filter");

    private String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
