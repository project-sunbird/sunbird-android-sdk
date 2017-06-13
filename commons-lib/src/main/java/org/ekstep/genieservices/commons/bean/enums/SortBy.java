package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 5/26/2017.
 *
 * @author anil
 */
public enum SortBy {

    RELEVANT("relevant"), NAME("name"), MOST_POPULAR("most_popular"), NEWEST("newest"), GENIE_SCORE("");

    private String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
