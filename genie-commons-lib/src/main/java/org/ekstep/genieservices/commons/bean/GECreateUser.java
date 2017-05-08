package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GECreateUser extends BaseTelemetry  {

    private final String eid = "GE_CREATE_USER";

    public GECreateUser(String gameID, String gameVersion, String uid, String loc) {
        super(gameID, gameVersion);
        setEks(createEKS(uid, loc));
        setUid(uid);
    }

    protected HashMap<String, Object> createEKS(String uid, String loc) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("uid", uid);
        eks.put("loc", loc);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

