package org.ekstep.genieservices.profile;

/**
 * Created on 25/4/17.
 * @author shriharsh
 */

public class SetAnonymousUserRequest {
    private String gameID;
    private String gameVersion;

    public SetAnonymousUserRequest() {
    }

    public SetAnonymousUserRequest(String gameID, String gameVersion) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
    }

    public String gameID() {
        return this.gameID;
    }

    public String gameVersion() {
        return this.gameVersion;
    }
}
