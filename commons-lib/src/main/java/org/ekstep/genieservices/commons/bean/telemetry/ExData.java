package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class ExData extends Telemetry {

    private static final String EID = "EXDATA";

    public ExData(String type, String data) {
        super(EID);
        setEData(createEData(type, data));
    }

    protected Map<String, Object> createEData(String type, String data) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", !StringUtil.isNullOrEmpty(type) ? type : "");
        eData.put("data", !StringUtil.isNullOrEmpty(data) ? data : "");
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
