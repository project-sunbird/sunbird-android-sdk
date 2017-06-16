package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 12/6/17.
 * shriharsh
 */

public class HierarchyInfo implements Serializable {

    private String identifier;
    private String contentType;

    public HierarchyInfo() {

    }

    public HierarchyInfo(String identifier, String contentType) {
        this.identifier = identifier;
        this.contentType = contentType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
