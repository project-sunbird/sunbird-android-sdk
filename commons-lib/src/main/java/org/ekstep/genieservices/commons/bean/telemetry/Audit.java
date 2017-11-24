package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Audit extends Telemetry {


    private static final String EID = "AUDIT";

    public Audit(List<String> props, String state, String prevstate, String actorType) {
        super(EID);
        setEData(createEData(props, state, prevstate));
        setActor(new Actor("", actorType));
    }

    protected Map<String, Object> createEData(List<String> props, String state, String prevstate) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("props", props != null ? props : new ArrayList<>());
        eData.put("state", !StringUtil.isNullOrEmpty(state) ? state : "");
        eData.put("prevstate", !StringUtil.isNullOrEmpty(prevstate) ? prevstate : "");
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
