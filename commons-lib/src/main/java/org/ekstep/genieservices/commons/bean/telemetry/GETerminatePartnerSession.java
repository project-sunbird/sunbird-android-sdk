package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GETerminatePartnerSession extends Telemetry {

    private static final String EID = "GE_STOP_PARTNER_SESSION";

    public GETerminatePartnerSession(String partnerID, String deviceId, Long length, String partnerSID) {
        super(EID);
        setEks(createEKS(partnerID, length, partnerSID));
        setDid(deviceId);
    }

    protected HashMap<String, Object> createEKS(String partnerID, Long length, String partnerSID) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("length", length);
        eks.put("sid", partnerSID);
        return eks;
    }
}
