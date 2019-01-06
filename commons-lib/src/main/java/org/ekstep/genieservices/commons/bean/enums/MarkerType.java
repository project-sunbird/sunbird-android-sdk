package org.ekstep.genieservices.commons.bean.enums;

public enum MarkerType {

    NOTHING(0), PREVIEWED(1), BOOKMARKED(2), WATCH_LATER(3);

    private int value;

    MarkerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
