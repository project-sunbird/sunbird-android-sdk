package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 6/1/2017.
 *
 * @author anil
 */
public enum ContentAccessStatus {

    NOT_PLAYED(0), PLAYED(1);

    private int value;

    ContentAccessStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
