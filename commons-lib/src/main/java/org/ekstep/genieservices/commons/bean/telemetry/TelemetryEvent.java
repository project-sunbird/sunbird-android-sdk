package org.ekstep.genieservices.commons.bean.telemetry;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Created by swayangjit on 10/5/17.
 */

public class TelemetryEvent {

    private String eid;
    private String ts;
    private Long ets;
    private String ver;
    private String sid;
    private String uid;
    private String did;
    private Map<String, Object> gdata;
    private Map<String, Object> edata;

    public TelemetryEvent(String eid, String ts, long ets, String ver, String sid, String uid,
                          String did, Map<String, Object> gdata, Map<String, Object> edata) {
        this.eid = eid;
        this.ts = ts;
        this.ets = ets;
        this.ver = ver;
        this.sid = sid;
        this.uid = uid;
        this.did = did;
        this.gdata = gdata;
        this.edata = edata;
    }

    public static TelemetryEvent buildFromMap(Map<String, Object> eventMap) {
        Map<String, Object> edataMap = (Map<String, Object>) eventMap.get("edata");
        Map<String, Object> gdataMap = (Map<String, Object>) eventMap.get("gdata");
        return new TelemetryEvent((String) eventMap.get("eid"),
                (String) eventMap.get("ts"),
                getEts(eventMap),
                (String) eventMap.get("ver"),
                (String) eventMap.get("sid"),
                (String) eventMap.get("uid"),
                (String) eventMap.get("did"),
                unmodifiableMap(gdataMap != null ? gdataMap : new HashMap<String, Object>()),
                unmodifiableMap(edataMap != null ? edataMap : new HashMap<String, Object>()));
    }

    private static Long getEts(Map<String, Object> eventMap) {
        if (eventMap.get("ets") == null) {
            return 0L;
        } else if (Long.class.equals(eventMap.get("ets").getClass())) {
            return (Long) eventMap.get("ets");
        } else if (Double.class.equals(eventMap.get("ets").getClass())) {
            return eventMap.get("ets") == null ? 0 : ((Double) eventMap.get("ets")).longValue();
        }
        return 0L;
    }

    public String eid() {
        return eid;
    }

    public String ts() {
        return ts;
    }

    public Long ets() {
        return ets;
    }

    public String ver() {
        return ver;
    }

    public String sid() {
        return sid;
    }

    public String uid() {
        return uid;
    }

    public String did() {
        return did;
    }

    public Map<String, Object> gdata() {
        return gdata;
    }

    public Map<String, Object> edata() {
        return edata;
    }
}
