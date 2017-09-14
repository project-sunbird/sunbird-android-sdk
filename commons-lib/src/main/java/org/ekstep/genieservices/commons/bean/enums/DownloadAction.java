package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created by swayangjit on 13/9/17.
 */

public enum DownloadAction {

    RESUME(0), PAUSE(1);

    private int value;

    DownloadAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
