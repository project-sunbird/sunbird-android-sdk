package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.HashMap;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GETerminatePartnerSession extends BaseTelemetry {

    private final String eid = "GE_STOP_PARTNER_SESSION";

    public GETerminatePartnerSession(GameData gameData, String partnerID,
                                     String deviceId, Long length, String partnerSID) {
        super(gameData);
        setEks(createEKS(partnerID, length, partnerSID));
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
    }

    protected HashMap<String, Object> createEKS(String partnerID, Long length, String partnerSID) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("length", length);
        eks.put("sid", partnerSID);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

