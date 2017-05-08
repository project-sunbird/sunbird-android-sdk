package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.CommonConstants;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GESendPartnerData extends BaseTelemetry {

    private final String eid = "GE_PARTNER_DATA";

    public GESendPartnerData(GameData gameData, String partnerID, String publicKeyID,
                             String deviceId, String data, String encryptedKey, String iv) {
        super(gameData);
        setEks(createEKS(partnerID, publicKeyID, data, encryptedKey, iv));
        setTags(partnerID);
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
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
    public String getEID() {
        return eid;
    }


    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public void setTags(String partnerId) {
        HashSet<String> partnerTags = new HashSet<>(Arrays.asList(partnerId));
        Map<String, Object> tag = new HashMap<>();
        tag.put(CommonConstants.PartnerPreference.KEY_PARTNER_ID, partnerTags);
        this.addTag(tag);
    }
}

