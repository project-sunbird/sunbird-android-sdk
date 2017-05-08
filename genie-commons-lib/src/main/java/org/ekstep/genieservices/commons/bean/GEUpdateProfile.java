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

public class GEUpdateProfile extends BaseTelemetry {

    private final String eid = "GE_UPDATE_PROFILE";


    public GEUpdateProfile(String gameID, String gameVersion, Profile profile, String deviceId) {
        super(gameID, gameVersion);
        setEks(createEKS(profile));
        setUid(profile.getUid());
        setTs(DateUtil.getCurrentTimestamp());
        setDid(deviceId);
    }

    private Map<String, Object> createEKS(Profile profile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("handle", profile.getHandle());
        map.put("age", profile.getAge());
        map.put("gender", profile.getGender());
        map.put("day", profile.getDay());
        map.put("month", profile.getMonth());
        map.put("standard", profile.getStandard());
        map.put("language", profile.getLanguage());
        map.put("is_group_user", profile.isGroupUser());
        map.put("medium", profile.getMedium());
        map.put("board", profile.getBoard());
        return map;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

