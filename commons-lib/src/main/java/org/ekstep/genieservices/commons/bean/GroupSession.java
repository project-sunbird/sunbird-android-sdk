package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the details about the current user session.
 */
public class GroupSession {
    private String gid;
    private String sid;
    private String createdTime;

    public GroupSession(String gid, String sid, String createdTime) {
        this.gid = gid;
        this.sid = sid;
        this.createdTime = createdTime;
    }

    public String getGid() {
        return gid;
    }

    public String getSid() {
        return sid;
    }

    public String getCreatedTime() {
        return this.createdTime;
    }

    public boolean isValid() {
        return !gid.isEmpty();
    }
}
