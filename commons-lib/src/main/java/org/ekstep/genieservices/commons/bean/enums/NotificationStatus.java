package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created by Indraja Machani on 6/19/2017.
 */

public enum NotificationStatus {

    READ("read"), UNREAD("unread"), ALL("all");

    private String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
