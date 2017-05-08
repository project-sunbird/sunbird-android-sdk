package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GERegisterPartner extends BaseTelemetry {

    private final String eid = "GE_REGISTER_PARTNER";

    public GERegisterPartner(String gameID, String gameVersion, String partnerID, String publicKey,
                             String publicKeyID, String deviceId) {
        super(gameID, gameVersion);
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

