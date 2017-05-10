package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/9/2017.
 *
 * @author anil
 */
public class ContentAccess implements Serializable {

    private String identifier;
    private String uid;
    private int status;
    private String contentType;
    private LearnerState learnerState;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LearnerState getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(LearnerState learnerState) {
        this.learnerState = learnerState;
    }
}