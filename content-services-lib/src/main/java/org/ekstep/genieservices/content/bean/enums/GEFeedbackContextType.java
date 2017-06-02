package org.ekstep.genieservices.content.bean.enums;

/**
 * Created on 5/29/2017.
 *
 * @author anil
 */
public enum GEFeedbackContextType {
    CONTENT("Content"), APP("App");

    private String value;

    GEFeedbackContextType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
