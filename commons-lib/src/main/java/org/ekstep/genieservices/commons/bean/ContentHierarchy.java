package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created by swayangjit on 1/6/17.
 */

public class ContentHierarchy implements Serializable {
    private String identifier;
    private String contentType;

    public ContentHierarchy(String identifier, String mediaType) {
        this.identifier = identifier;
        this.contentType = mediaType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMediaType() {
        return contentType;
    }

    public void setMediaType(String mediaType) {
        this.contentType = mediaType;
    }
}
