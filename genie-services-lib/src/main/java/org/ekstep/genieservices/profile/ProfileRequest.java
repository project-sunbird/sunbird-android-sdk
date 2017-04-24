package org.ekstep.genieservices.profile;

/**
 * Created on 24/4/17.
 *
 * @author shriharsh
 */

public class ProfileRequest {
    private String appID;
    private String appVersion;
    private String profileJson;

    public ProfileRequest(String profileJson) {
        this.profileJson = profileJson;
    }

    public ProfileRequest(String gameID, String gameVersion, String profileJson) {
        this.appID = gameID;
        this.appVersion = gameVersion;
        this.profileJson = profileJson;
    }

    public String appID() {
        return this.appID;
    }

    public String appVersion() {
        return this.appVersion;
    }

    public String profileJson() {
        return this.profileJson;
    }
}
