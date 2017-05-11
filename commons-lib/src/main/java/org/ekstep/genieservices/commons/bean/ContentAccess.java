package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.Map;

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
    private Map<String, Object> learnerState;

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

    public Map<String, Object> getLearnerState() {
        return learnerState;
    }

    public void setLearnerState(Map<String, Object> learnerState) {
        this.learnerState = learnerState;
    }
}