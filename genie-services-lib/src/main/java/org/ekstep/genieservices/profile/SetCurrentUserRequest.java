package org.ekstep.genieservices.profile;

/**
 * Created on 25/4/17.
 *
 * @author shriharsh
 */

public class SetCurrentUserRequest {
    private String gameID;
    private String gameVersion;
    private String uid;

    public SetCurrentUserRequest(String uid) {
        this.uid = uid;
    }

    public SetCurrentUserRequest(String gameID, String gameVersion, String uid) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.uid = uid;
    }

    public String gameID() {
        return this.gameID;
    }

    public String gameVersion() {
        return this.gameVersion;
    }

    public String uid() {
        return this.uid;
    }
}
