package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentType;

/**
 * Created on 5/11/2017.
 *
 * @author anil
 */
public class ContentAccessCriteria {

    private String contentIdentifier;
    private String uid;
    private ContentType[] contentTypes;

    public String getContentIdentifier() {
        return contentIdentifier;
    }

    public void setContentIdentifier(String contentIdentifier) {
        this.contentIdentifier = contentIdentifier;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ContentType[] getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(ContentType[] contentTypes) {
        this.contentTypes = contentTypes;
    }
}
