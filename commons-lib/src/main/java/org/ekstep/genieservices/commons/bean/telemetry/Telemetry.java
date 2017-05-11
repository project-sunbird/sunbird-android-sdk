package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.CoRelation;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swayangjit on 27/4/17.
 */

public class Telemetry {

    private static final String TELEMETRY_VERSION = "2.0";
    private static final String GENIE_SERVICE_GID = "genieservice.android";

    private final Map<String, Object> edata = new HashMap<>();
    private String ts;
    private String ver;
    private String sid = "";
    private String uid = "";
    private String did;
    private String eid;
    private long ets;
    private GameData gdata;
    private List<Map<String, Object>> tags = new ArrayList();
    private List<CoRelation> cdata;

    public Telemetry(GameData gameData, String eid) {
        setGdata(gameData);
        this.eid=eid;
        this.ver = TELEMETRY_VERSION;
        this.ets = DateUtil.getEpochTime();
    }

    public void addCoRelation(List<CoRelation> coRelation) {
        this.cdata = new ArrayList<>();
        if (coRelation != null) {
            this.cdata.addAll(coRelation);
        }
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

    public Map<String, Object> getEData() {
        return this.edata;
    }

    public GameData getGdata() {
        return this.gdata;
    }

    public void setGdata(GameData gameData) {
        if(gameData!=null && !isValidId(gameData.getId())){
            gameData.setId(GENIE_SERVICE_GID);
        }
        this.gdata = gameData;
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

    public String getEid() {
        return this.eid;
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

    protected boolean isValidId(String gameID) {
        return gameID != null && !gameID.trim().isEmpty();
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
