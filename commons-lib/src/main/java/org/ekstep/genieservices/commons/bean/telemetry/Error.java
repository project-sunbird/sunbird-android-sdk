package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swayangjit on 15/11/17.
 */

public class Error extends TelemetryV3 {

    private static final String EID = "ERROR";

    private Error(String err, String stackTrace, String pageId) {
        super(EID);
        setEData(createEData(err, stackTrace, pageId));
    }

    protected Map<String, Object> createEData(String err, String stackTrace, String pageId) {
        Map<String, Object> eks = new HashMap<>();
        eks.put("err", err);
        eks.put("errtype", "MOBILEAPP");
        eks.put("stacktrace", stackTrace);
        eks.put("pageid", !StringUtil.isNullOrEmpty(pageId) ? pageId : "");
        return eks;
    }

}
