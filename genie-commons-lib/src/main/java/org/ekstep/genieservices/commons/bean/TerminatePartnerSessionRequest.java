package org.ekstep.genieservices.commons.bean;

/**
 * Created on 2/5/17.
 *
 * @author shriharsh
 */

public class TerminatePartnerSessionRequest {
    private String gameID;
    private String gameVersion;
    private String partnerID;

    public TerminatePartnerSessionRequest(String partnerID) {
        this.partnerID = partnerID;
    }

    public TerminatePartnerSessionRequest(String gameID, String gameVersion, String partnerID) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.partnerID = partnerID;
    }

    public String gameID() {
        return this.gameID;
    }

    public String gameVersion() {
        return this.gameVersion;
    }

    public String partnerID() {
        return this.partnerID;
    }
}
