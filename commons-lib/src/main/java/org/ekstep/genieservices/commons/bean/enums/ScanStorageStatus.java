package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 9/10/17.
 * shriharsh
 */

public enum  ScanStorageStatus {

    ADDED(1), DELETED(2), UPDATED(2), NO_CHANGES(3);

    private int value;

    ScanStorageStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
