package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GEError extends Telemetry {

    private static final String EID = "GE_ERROR";

    public GEError(String err, String id, String eventId, String data) {
        super(EID);
        setEks(createEKS(err, id, eventId, data));
    }

    protected HashMap<String, Object> createEKS(String err, String id, String eventId, String data) {
        HashMap<String, Object> eks = new HashMap<>();
        eks.put("err", err);
        eks.put("id", id);
        eks.put("eventId", eventId);
        eks.put("data", data);
        eks.put("type", "GENIESDK");
        return eks;
    }
}

