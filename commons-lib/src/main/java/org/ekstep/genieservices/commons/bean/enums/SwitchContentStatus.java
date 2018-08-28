package org.ekstep.genieservices.commons.bean.enums;


public enum SwitchContentStatus {
    SWITCH_SUCCESSFUL(0), SWITCH_FAILED(1);

    private int value;

    SwitchContentStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
