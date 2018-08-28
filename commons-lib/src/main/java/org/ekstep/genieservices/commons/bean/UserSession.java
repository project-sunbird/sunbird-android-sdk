package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the details about the current user session.
 */
public class UserSession {
    private String uid;
    private String sid;
    private String createdTime;

    public UserSession(String uid, String sid, String createdTime) {
        this.uid = uid;
        this.sid = sid;
        this.createdTime = createdTime;
    }

    public String getUid() {
        return uid;
    }

    public String getSid() {
        return sid;
    }

    public String getCreatedTime() {
        return this.createdTime;
    }

    public boolean isValid() {
        return !uid.isEmpty();
    }
}
