package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 6/1/2017.
 *
 * @author anil
 */
public enum ContentAccessStatusType {

    NOT_PLAYED(0), PLAYED(1);

    private int value;

    ContentAccessStatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
