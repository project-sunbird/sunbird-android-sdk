package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Feedback extends Telemetry {

    private static final String EID = "FEEDBACK";

    public Feedback(float rating, String comments, String id, String version, String type) {
        super(EID);
        setEData(createData(rating, comments));
        setObject(id, version, type);
    }

    private Map<String, Object> createData(float rating, String comments) {
        HashMap<String, Object> eData = new HashMap<>();
        eData.put("rating", rating);
        eData.put("comments", comments);
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
