package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GESessionEnd extends BaseTelemetry {

    private final String eid = "GE_SESSION_END";

    public GESessionEnd(String gameID, String gameVersion, UserSession session, String deviceId) {
        super(gameID, gameVersion);
        setEks(createEKS(session.getCreatedTime()));
        setSid(session.getSid());
        setUid(session.getUid());
        setDid(deviceId);
        setTs(DateUtil.getCurrentTimestamp());
    }

    protected HashMap<String, Object> createEKS(DateTime createdTime) {
        DateTime now = DateTime.now();
        Seconds seconds = Seconds.secondsBetween(createdTime, now);
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("length", seconds.getSeconds());
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

