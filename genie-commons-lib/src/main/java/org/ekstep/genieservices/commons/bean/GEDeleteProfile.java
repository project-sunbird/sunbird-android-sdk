package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEDeleteProfile extends BaseTelemetry implements ITelemetry {

    private final String eid = "GE_DELETE_PROFILE";

    public GEDeleteProfile(Profile profile, String gameID, String gameVersion) {
        super(gameID, gameVersion);
        setEks(createEKS(profile));
        setUid(profile.getUid());
    }

    private Map<String, Object> createEKS(Profile profile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("duration", getDuration(profile.getCreatedAt(), new Date()));
        return map;
    }

    private long getDuration(Date profileCreationDate, Date currentDate) {
        return currentDate.getTime() - profileCreationDate.getTime();
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
