package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 9/10/17.
 * shriharsh
 */

public enum MoveContentStatus {
    HIGHER_VERSION_IN_DESTINATION(1), LOWER_VERSION_IN_DESTINATION(2);

    private int value;

    MoveContentStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
