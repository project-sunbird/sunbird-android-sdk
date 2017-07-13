package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GEStartPartnerSession extends Telemetry {

    private static final String EID = "GE_START_PARTNER_SESSION";

    public GEStartPartnerSession(String partnerID, String deviceId, String partnerSID) {
        super(EID);
        setEks(createEKS(partnerID, partnerSID));
        setDid(deviceId);
    }

    protected HashMap<String, Object> createEKS(String partnerID, String partnerSID) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("sid", partnerSID);
        return eks;
    }

}
