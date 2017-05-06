package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.IValidate;

import java.util.List;

/**
 * Created on 2/5/17.
 *
 * @author shriharsh
 */

public class PartnerData implements IValidate {
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String partnerData;
    private String publicKey;

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public List<String> getErrors() {
        return null;
    }

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

    public String getGameID() {
        return this.gameID;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public String getPartnerID() {
        return this.partnerID;
    }

    public String getPartnerData() {
        return this.partnerData;
    }
}
