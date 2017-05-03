package org.ekstep.genieservices.commons.bean;

/**
 * Created on 2/5/17.
 *
 * @author shriharsh
 */

public class PartnerDataRequest {
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String partnerData;

    public PartnerDataRequest(String partnerID, String partnerData) {
        this.partnerID = partnerID;
        this.partnerData = partnerData;
    }

    public PartnerDataRequest(String gameID, String gameVersion, String partnerID, String partnerData) {
        this.gameID = gameID;
        this.gameVersion = gameVersion;
        this.partnerID = partnerID;
        this.partnerData = partnerData;
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
