package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GEUpdateProfile extends Telemetry {

    private static final String EID = "GE_UPDATE_PROFILE";

    public GEUpdateProfile(Profile profile, String deviceId) {
        super(EID);
        setEks(createEKS(profile));
        setUid(profile.getUid());
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

}
