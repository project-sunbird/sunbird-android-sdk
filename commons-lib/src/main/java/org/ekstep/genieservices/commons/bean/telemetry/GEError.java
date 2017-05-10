package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

import java.util.HashMap;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GEError extends BaseTelemetry {

    private final String eid = "GE_ERROR";

    public GEError(GameData gameData, String err, String id, String eventId, String data) {
        super(gameData);
        String type = isValidId(gameData.getId()) ? "GENIESERVICES" : "PARTNERAPP";
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

