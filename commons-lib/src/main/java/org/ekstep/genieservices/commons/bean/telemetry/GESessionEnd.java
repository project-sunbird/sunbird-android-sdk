package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GESessionEnd extends Telemetry {

    private static final String EID = "GE_SESSION_END";

    public GESessionEnd(UserSession session, String deviceId) {
        super(EID);
        setEks(createEKS(session.getCreatedTime()));
        setSid(session.getSid());
        setUid(session.getUid());
        setDid(deviceId);
    }

    protected HashMap<String, Object> createEKS(String createdTime) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("length", DateUtil.elapsedTimeTillNow(createdTime));
        return eks;
    }
}

