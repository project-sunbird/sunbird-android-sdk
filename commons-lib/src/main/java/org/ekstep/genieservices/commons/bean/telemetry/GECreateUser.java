package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

import java.util.HashMap;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GECreateUser extends Telemetry {

    private static final String EID = "GE_CREATE_USER";

    public GECreateUser(GameData gameData, String uid, String loc) {
        super(gameData, EID);
        setEks(createEKS(uid, loc));
        setUid(uid);
    }

    protected HashMap<String, Object> createEKS(String uid, String loc) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("uid", uid);
        eks.put("loc", loc);
        return eks;
    }

}

