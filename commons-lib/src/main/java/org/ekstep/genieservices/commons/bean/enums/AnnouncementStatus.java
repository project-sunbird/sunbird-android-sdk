package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 04/03/2018.
 *
 * @author Indraja Machani
 */
public enum AnnouncementStatus {

    RECEIVED("received"), READ("read");

    private String value;

    AnnouncementStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
