package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.ContentType;

/**
 * Created on 5/12/2017.
 *
 * @author anil
 */
public class ContentCriteria {

    private String uid;
    private ContentType[] contentTypes;
    private boolean attachFeedback;
    private boolean attachContentAccess;

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

    public boolean isAttachFeedback() {
        return attachFeedback;
    }

    public void setAttachFeedback(boolean attachFeedback) {
        this.attachFeedback = attachFeedback;
    }

    public boolean isAttachContentAccess() {
        return attachContentAccess;
    }

    public void setAttachContentAccess(boolean attachContentAccess) {
        this.attachContentAccess = attachContentAccess;
    }
}
