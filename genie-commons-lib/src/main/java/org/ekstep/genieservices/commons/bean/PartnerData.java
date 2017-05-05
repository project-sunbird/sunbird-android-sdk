package org.ekstep.genieservices.commons.bean;

/**
 * Created on 2/5/17.
 *
 * @author shriharsh
 */

public class PartnerData {
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String partnerData;
    private String publicKey;

    public PartnerData(String gameID, String gameVersion, String partnerID, String partnerData, String publicKey) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.partnerID = partnerID;
        this.partnerData = partnerData;
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
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

    public String partnerData() {
        return this.partnerData;
    }
}
