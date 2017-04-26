package org.ekstep.genieservices.profile;

/**
 * Created on 25/4/17.
 * @author shriharsh
 */

public class UnsetCurrentUserRequest {
    private String gameID;
    private String gameVersion;

    public UnsetCurrentUserRequest() {
    }

    public UnsetCurrentUserRequest(String gameID, String gameVersion) {
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
