package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.UserSession;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GESessionStart extends Telemetry {

    private static final String EID = "GE_SESSION_START";

    public GESessionStart(UserSession session, String loc, String deviceId) {
        super(EID);
        setEks(createEKS(loc));
        setSid(session.getSid());
        setUid(session.getUid());
        setDid(deviceId);
    }

    protected HashMap<String, Object> createEKS(String loc) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("loc", loc);
        return eks;
    }
}
