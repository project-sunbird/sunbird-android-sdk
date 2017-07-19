package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GEDeleteProfile extends Telemetry {

    private static final String EID = "GE_DELETE_PROFILE";

    public GEDeleteProfile(Profile profile) {
        super(EID);
        setEks(createEKS(profile));
        setUid(profile.getUid());
    }

    private Map<String, Object> createEKS(Profile profile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("duration", DateUtil.elapsedTimeTillNow(profile.getCreatedAt().getTime()));
        return map;
    }
}
