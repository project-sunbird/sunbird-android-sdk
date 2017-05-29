package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.IValidate;

import java.util.List;

/**
 * This class is used to store the Partner Details.
 * <p>
 * And also to check the validity of the data, it implements interface {@link IValidate}
 * <p>
 * <p>
 * <p>
 * Below are the parameters that constructor of this class accepts
 * <p>
 * <p>
 * gameID - Unique Identifier of the Partner
 * <p>
 * gameVersion - Version of the Partner App
 * <p>
 * partnerID - Unique Identifier of the Partner
 * <p>
 * partnerData - Any relevant data need to be passed
 * <p>
 * publicKey - Provide an appropriate RSA Public Key as a String in PEM format
 * with the delimiter.
 * <p>
 * <p>Example:</p>
 * <p/>
 * <p>-----BEGIN PUBLIC KEY-----
 * MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGdo5VYOK9cTrQQ+ajOxfHMgg/
 * TDX77o/eVTUjcErLLYKBQ6qb8t/jCCuRNexIexRBldk4gC9STyuVWN8x2xkSildf
 * Nch3KUTvwgJx1n2y/03tIHkimOxEONCg3rWPdiWx7nLdW4TuHbwZTZmMdhLjM4lI
 * OSyoyYpX/JmDnxjq4QIDAQAB
 * -----END PUBLIC KEY-----</p>
 * <p/>
 * <p>To generate an RSA Private key pair use,</p>
 * <pre>openssl genrsa -out rsa_1024_priv.pem 1024</pre>
 * <p>To generate the Public Key in PEM format use,</p>
 * <pre>openssl rsa -pubout -in rsa_1024_priv.pem -out rsa_1024_pub.pem</pre>
 * <p/>
 */

public class PartnerData implements IValidate {
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

    public String   getPartnerData() {
        return this.partnerData;
    }
}
