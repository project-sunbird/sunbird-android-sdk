package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds the data about the Telemetry event
 */
public class Telemetry {

    private static final String TELEMETRY_VERSION = "2.1";

    /**
     * unique event ID.
     */
    private String eid;

    /**
     * timestamp of event capture in YYYY-MM-DDThh:mm:ss+/-nn:nn format, timezone is mandatory.
     */
    private String ts;

    /**
     * epoch timestamp of event capture in epoch format (time in milli-seconds. For ex: 1442816723).
     */
    private long ets;

    /**
     * version of the event data structure, currently "2.1".
     */
    private String ver;

    /**
     * Channel ID.
     */
    private String channel;
    private ProducerData pdata;
    private GameData gdata;
    private List<CorrelationData> cdata;
    private String sid = "";
    private String uid = "";
    private String did;
    private Map<String, Object> edata = new HashMap<>();

    // TODO: 7/13/2017 - Remove tag and etag
    private List<Map<String, Object>> tags = new ArrayList<>();

    public Telemetry(String eid) {
        this.eid = eid;
        this.ver = TELEMETRY_VERSION;
        this.ts = DateUtil.getCurrentTimestamp();
        this.ets = DateUtil.getEpochTime();
    }

    public String getEid() {
        return this.eid;
    }

    public String getTs() {
        return this.ts;
    }

    public long getEts() {
        return this.ets;
    }

    public String getVer() {
        return this.ver;
    }

    public GameData getGdata() {
        return this.gdata;
    }

    public void setGdata(GameData gameData) {
        this.gdata = gameData;
    }

    public List<CorrelationData> getCdata() {
        return cdata;
    }

    public void addCorrelationData(List<CorrelationData> correlationData) {
        this.cdata = new ArrayList<>();
        if (correlationData != null) {
            this.cdata.addAll(correlationData);
        }
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Map<String, Object> getEData() {
        return this.edata;
    }

    public void addTag(Map<String, Object> map) {
        this.tags.add(map);
    }

    public void setEks(Map<String, Object> eks) {
        edata.put("eks", eks);
    }

    public void setEks(Object eks) {
        edata.put("eks", eks);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
