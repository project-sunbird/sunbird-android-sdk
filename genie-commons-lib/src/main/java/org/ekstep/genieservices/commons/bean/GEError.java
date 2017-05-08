package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.ITelemetry;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEError extends BaseTelemetry {

    private final String eid = "GE_ERROR";

    public GEError(String gameID, String gameVersion, String err, String id, String eventId, String data) {
        super(gameID, gameVersion);
        String type = isValidId(gameID) ? "GENIESERVICES" : "PARTNERAPP";
        setEks(createEKS(err, id, eventId, data, type));

    }

    protected HashMap<String, Object> createEKS(String err, String id, String eventId, String data, String type) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("err", err);
        eks.put("id", id);
        eks.put("eventId", eventId);
        eks.put("data", data);
        eks.put("type", type);
        return eks;
    }

    @Override
    public String getEID() {
        return eid;
    }

}

