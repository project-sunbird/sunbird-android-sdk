package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.IValidate;

import java.util.List;

/**
 * This class is used to store the Partner Details.
 * <p>
 * And also to check the validity of the contents in this class, it implements interface {@link IValidate}
 *
 */

public class PartnerData implements IValidate {
    private String gameID;
    private String gameVersion;
    private String partnerID;
    private String partnerData;
    private String publicKey;

    @Override
    public boolean isValid() {
        // TODO: 10/5/17 It cannot be false all time, need to add logic for it
        return false;
    }

    @Override
    public List<String> getErrors() {
        // TODO: 10/5/17 Errors cannot be null, needs to check to how it has to be done
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
