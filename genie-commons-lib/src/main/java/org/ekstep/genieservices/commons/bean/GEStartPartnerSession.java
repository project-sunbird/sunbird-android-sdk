package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEStartPartnerSession extends BaseTelemetry {

    private final String eid = "GE_START_PARTNER_SESSION";

    public GEStartPartnerSession(String gameID, String gameVersion, String partnerID, String deviceId, String partnerSID) {
        super(gameID, gameVersion);
        setEks(createEKS(partnerID, partnerSID));
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
    }

    protected HashMap<String, Object> createEKS(String partnerID, String partnerSID) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("sid", partnerSID);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

