package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.Profile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEDeleteProfile extends BaseTelemetry{

    private final String eid = "GE_DELETE_PROFILE";

    public GEDeleteProfile(GameData gameData, Profile profile) {
        super(gameData);
        setEks(createEKS(profile));
        setUid(profile.getUid());
    }

    private Map<String, Object> createEKS(Profile profile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("duration",  new Date().getTime() -profile.getCreatedAt().getTime());;
        return map;
    }
    @Override
    public String getEID() {
        return eid;
    }
}
