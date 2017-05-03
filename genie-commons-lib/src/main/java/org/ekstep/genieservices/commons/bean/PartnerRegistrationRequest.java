package org.ekstep.genieservices.commons.bean;

/**
 * Created on 28/4/17.
 * @author shriharsh
 *
 */

public class PartnerRegistrationRequest {
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String publicKey;

    public PartnerRegistrationRequest(String partnerID, String publicKey) {
        this.partnerID = partnerID;
        this.publicKey = publicKey;
    }

    public PartnerRegistrationRequest(String gameID, String gameVersion, String partnerID, String publicKey) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.partnerID = partnerID;
        this.publicKey = publicKey;
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

    public String publicKey() {
        return this.publicKey;
    }
}
