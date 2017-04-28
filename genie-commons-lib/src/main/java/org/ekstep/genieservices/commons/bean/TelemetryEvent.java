package org.ekstep.genieservices.commons.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 27/4/17.
 */

public abstract class TelemetryEvent {
    private String ts;
    private String ver;
    private String sid = "";
    private String uid = "";
    private String did;
    private long ets;
    private GameData gdata;
    private List<Map<String, Object>> tags = new ArrayList();

    public void addTag(Map<String, Object> map) {
        this.tags.add(map);
    }

    public TelemetryEvent() {
    }

    public GameData getGdata() {
        return this.gdata;
    }

    public void setGdata(GameData gdata) {
        this.gdata = gdata;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
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

    public long getEts() {
        return this.ets;
    }

    public void setEts(long ets) {
        this.ets = ets;
    }
}
