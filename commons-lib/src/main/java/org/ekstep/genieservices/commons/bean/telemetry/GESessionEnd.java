package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.HashMap;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GESessionEnd extends Telemetry {

    private static final String EID = "GE_SESSION_END";

    public GESessionEnd(GameData gameData, UserSession session, String deviceId) {
        super(gameData,EID);
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
}

