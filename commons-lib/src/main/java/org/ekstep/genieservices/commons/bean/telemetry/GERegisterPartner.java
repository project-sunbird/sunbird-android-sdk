package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.HashMap;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GERegisterPartner extends BaseTelemetry {

    private final String eid = "GE_REGISTER_PARTNER";

    public GERegisterPartner(GameData gameData, String partnerID, String publicKey,
                             String publicKeyID, String deviceId) {
        super(gameData);
        setEks(createEKS(partnerID, publicKey, publicKeyID));
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
    }

    protected HashMap<String, String> createEKS(String partnerID, String publicKey, String publicKeyID) {
        HashMap<String, String> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("publickey", publicKey);
        eks.put("publickeyid", publicKeyID);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

