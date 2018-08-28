package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 10/3/2017.
 *
 * @author anil
 */
public enum ContentDeleteStatus {

    NOT_FOUND(-1), DELETED_SUCCESSFULLY(1);

    private int value;

    ContentDeleteStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
