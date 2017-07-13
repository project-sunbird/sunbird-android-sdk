package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GERegisterPartner extends Telemetry {

    private static final String EID = "GE_REGISTER_PARTNER";

    public GERegisterPartner(String partnerID, String publicKey, String publicKeyID, String deviceId) {
        super(EID);
        setEks(createEKS(partnerID, publicKey, publicKeyID));
        setDid(deviceId);
    }

    protected HashMap<String, String> createEKS(String partnerID, String publicKey, String publicKeyID) {
        HashMap<String, String> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("publickey", publicKey);
        eks.put("publickeyid", publicKeyID);
        return eks;
    }

}

