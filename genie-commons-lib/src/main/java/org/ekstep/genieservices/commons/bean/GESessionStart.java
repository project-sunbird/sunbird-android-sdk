package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GESessionStart extends BaseTelemetry implements ITelemetry {

    private final String eid = "GE_SESSION_START";
    private final Map<String, Object> edata;

    public GESessionStart(UserSession session, String loc, String deviceId, String gameID, String gameVersion) {
        super(gameID, gameVersion);
        edata = new HashMap<>();
        setEks(createEKS(loc));
        setEts(DateUtil.getEpochTime());
        setSid(session.getSid());
        setUid(session.getUid());
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
    }

    protected HashMap<String, Object> createEKS(String loc) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("loc", loc);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<String> getErrors() {
        return null;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}

