package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 1/5/17.
 */

public class GECreateProfile extends BaseTelemetry {

    private final String eid = "GE_CREATE_PROFILE";

    public GECreateProfile(GameData gameData, Profile profile, String loc) {
        super(gameData);
        setEks(createEKS(profile, loc));
        setUid(profile.getUid());
    }

    private Map<String, Object> createEKS(Profile profile, String loc) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", profile.getUid());
        map.put("handle", profile.getHandle());
        map.put("age", profile.getAge());
        map.put("gender", profile.getGender());
        map.put("day", profile.getDay());
        map.put("month", profile.getMonth());
        map.put("standard", profile.getStandard());
        map.put("language", profile.getLanguage());
        map.put("loc", loc);
        map.put("is_group_user", profile.isGroupUser());
        if (!StringUtil.isNullOrEmpty(profile.getAvatar())) {
            String[] avatarArr = profile.getAvatar().split("/");
            if (avatarArr != null && avatarArr.length > 1) {
                map.put("avatar", avatarArr[1]);
            }
        }
        map.put("medium", profile.getMedium());
        map.put("board", profile.getBoard());
        return map;
    }

    @Override
    public String getEID() {
        return eid;
    }

}