package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Feedback extends TelemetryV3 {

    private static final String EID = "FEEDBACK";

    public Feedback(float rating, String comments) {
        super(EID);
        setEData(createData(rating, comments));
    }

    private Map<String, Object> createData(float rating, String comments) {
        HashMap<String, Object> eData = new HashMap<>();
        eData.put("rating", rating);
        eData.put("comments", comments);
        return eData;
    }
}
