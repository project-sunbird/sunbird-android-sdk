package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GECreateUser extends Telemetry {

    private static final String EID = "GE_CREATE_USER";

    public GECreateUser(String uid, String loc) {
        super(EID);
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
