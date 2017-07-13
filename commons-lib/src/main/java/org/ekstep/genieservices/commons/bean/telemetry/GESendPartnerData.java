package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GESendPartnerData extends Telemetry {

    private static final String EID = "GE_PARTNER_DATA";

    public GESendPartnerData(String partnerID, String publicKeyID, String deviceId, String data, String encryptedKey, String iv) {
        super(EID);
        setEks(createEKS(partnerID, publicKeyID, data, encryptedKey, iv));
        setTags(partnerID);
        setDid(deviceId);
    }

    protected HashMap<String, String> createEKS(String partnerID, String publicKeyID, String data, String encryptedKey, String iv) {
        HashMap<String, String> eks = new HashMap<>();
        eks.put("partnerid", partnerID);
        eks.put("publickeyid", publicKeyID);
        eks.put("data", data);
        eks.put("key", encryptedKey);
        eks.put("iv", iv);
        return eks;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    private void setTags(String partnerId) {
        ETags ETags = new ETags();
        ETags.setPartner(Collections.singletonList(partnerId));
        this.setEtags(ETags);
    }
}

